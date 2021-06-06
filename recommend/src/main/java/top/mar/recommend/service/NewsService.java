package top.mar.recommend.service;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import top.mar.recommend.consts.NConstants;
import top.mar.recommend.listener.InitDataListener;
import top.mar.recommend.mapper.NewsMapper;
import top.mar.recommend.mapper.UserMapper;
import top.mar.recommend.model.News;
import top.mar.recommend.model.NewsDetail;
import top.mar.recommend.model.User;
import top.mar.recommend.utils.JsonUtils;
import top.mar.recommend.utils.RandomUtils;
import top.mar.recommend.utils.RedisUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewsService {

    private static Logger logger = LoggerFactory.getLogger(NewsService.class);

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    NewsMapper newsMapper;

    @Autowired
    UserMapper userMapper;

    public int getViewCount(User user) {
        String key = NConstants.NewsConfig.USER_VIEW_NEWS_PREFIX + user.getOpenid();
        return redisUtils.getSetCounts(key);
    }

    // 获得用户感兴趣的topK个主题以及对应的评分
    public Set<ZSetOperations.TypedTuple<String>> getFavTopic(User user) {
        String key = NConstants.NewsConfig.USER_TOPIC_PREFIX + user.getOpenid();
        return redisUtils.getZSetByRank(key, 0, NConstants.NewsConfig.ROUGH_TOPIC_COUNT - 1);
    }

    // 获得 分类-新闻 和 已推送新闻的差集
    public Set<ZSetOperations.TypedTuple<String>> getUnScanNews(User user, String cat) {
        return redisUtils.zSetSubSet(NConstants.NewsConfig.CAT_NEWS_PREFIX + cat,
                NConstants.NewsConfig.USER_SCAN_NEWS_PREFIX + user.getOpenid(),
                NConstants.NewsConfig.TMP_USER_UN_SCAN_NEWS_PREFIX + user.getOpenid());
    }

    // 标记为已经推送过的新闻
    public void markAsScanNews(User user, List<String> newsList) {
        if (newsList == null || newsList.size() == 0) return;
        redisUtils.addListToSet(NConstants.NewsConfig.USER_SCAN_NEWS_PREFIX + user.getOpenid(), newsList);
    }

    public List<String> getRoughNewsList(User user, String cat) {
        logger.info("开始粗排...");
        Set<ZSetOperations.TypedTuple<String>> favTopicSet = getFavTopic(user);
        List<String> roughNewsList = new ArrayList<>();
        double totalScore = 0.0;
        for (ZSetOperations.TypedTuple<String> keyValue : favTopicSet) {
            totalScore += keyValue.getScore();
        }

        // cat - scan = 可推送的新闻集合
        Set<ZSetOperations.TypedTuple<String>> unScanNews = getUnScanNews(user, cat);
        Set<String> realUnScanNews = RedisUtils.transferSetWithScoreToNormalSet(unScanNews);

        for (ZSetOperations.TypedTuple<String> keyValue : favTopicSet) {

            // 向上取整，获得该主题应该分配的新闻数目
            int nowCount = (int) Math.ceil(keyValue.getScore() / totalScore *
                    (double) NConstants.NewsConfig.ROUGH_RECOMMEND_COUNT);

            // 可推送的新闻集合与主题集合取交集 = 该主题的可推送集合
            Set<ZSetOperations.TypedTuple<String>> topicRoughSet =
                    redisUtils.interZSet(NConstants.NewsConfig.TOPIC_NEWS_PREFIX + keyValue.getValue(), realUnScanNews);
            // 将集合转成list
            List<String> partRoughList = new ArrayList<>();
            for (ZSetOperations.TypedTuple<String> kv : topicRoughSet) {
                partRoughList.add(kv.getValue());
            }

            // 根据随机策略获取应该选取的下标
            List<Integer> indexList = RandomUtils.randomIndexWithAvgStrategy(topicRoughSet.size(), nowCount);
            for (int nowIndex : indexList) {
                // 不需要对size进行限制，因为实际我们粗排的数目并不严格要求
                roughNewsList.add(partRoughList.get(nowIndex));
            }

            logger.info("topic:{} 应分配新闻数量:{}, 实际获取新闻数量:{}",
                    keyValue.getValue(), nowCount, indexList.size());
            logger.info("新闻详细列表:{}", partRoughList);

        }
        logger.info("粗排完成, 得出用户感兴趣新闻:{}篇, 具体为:{}", roughNewsList.size(), roughNewsList);
        return roughNewsList;
    }

    public List<String> getCarefulNewsList(User user, List<String> roughNewsList, int calCount) {
        if (roughNewsList == null || roughNewsList.size() == 0) return new ArrayList<>();
        logger.info("开始精排...");
        List<String> carefulNewsList = new ArrayList<>();

        // 获取用户-主题 map
        Set<ZSetOperations.TypedTuple<String>> userTopicSet =
                redisUtils.getZSetByRank(NConstants.NewsConfig.USER_TOPIC_PREFIX + user.getOpenid(), 0, -1);
        HashMap<String, Double> userTopicMap = new HashMap<>();
        for (ZSetOperations.TypedTuple<String> keyValue : userTopicSet) {
            userTopicMap.put(keyValue.getValue(), keyValue.getScore());
        }
        logger.info("用户-主题数据:{}", userTopicMap);

        // 获取新闻-评分 map
        Map<String, Double> newsScoreMap = new TreeMap<>();
        for (String docId : roughNewsList) {
            double docScore = 0;
            Set<ZSetOperations.TypedTuple<String>> newsTopicSet =
                    InitDataListener.newsTopicGlobalMap.get(NConstants.NewsConfig.NEWS_TOPIC_PREFIX + docId);
            for (ZSetOperations.TypedTuple<String> keyValue : newsTopicSet) {
                docScore += keyValue.getScore() * userTopicMap.getOrDefault(keyValue.getValue(), 0.0);
            }
            newsScoreMap.put(docId, docScore);
        }

        List<Map.Entry<String, Double>> newsScoreList = new ArrayList<>(newsScoreMap.entrySet());
        //降序排序
        Collections.sort(newsScoreList, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        logger.info("精排新闻&评分排序如下:{}", newsScoreList);

        for (int index = 0; index < newsScoreList.size(); index++) {
            if (index == calCount) break;
            carefulNewsList.add(newsScoreList.get(index).getKey());
        }
        logger.info("精排完成, 得出推荐文章:{}篇, 具体为:{}", carefulNewsList.size(), carefulNewsList);

        return carefulNewsList;
    }

    public List<String> getHotNewsList(User user, String cat, int hotCount) {

        logger.info("开始热门推荐...");
        List<String> hotNewsList = new ArrayList<>();

        // cat - scan = 可推送的新闻集合
        Set<ZSetOperations.TypedTuple<String>> unScanNews = getUnScanNews(user, cat.equals("hot") ? "all" : cat);
        List<ZSetOperations.TypedTuple<String>> partHotNewsList = new ArrayList<>();
        logger.info("可以推送的新闻数目为:{}", unScanNews.size());
        // 过滤掉小于一定阈值的新闻
        for (ZSetOperations.TypedTuple<String> keyValue : unScanNews) {
            if (keyValue.getScore() >= 1.0 * NConstants.NewsConfig.HOT_RECOMMEND_LEVEL) {
                partHotNewsList.add(keyValue);
            }
        }
        logger.info("可以推送的新闻中，评论数超过:{}的有:{}篇", NConstants.NewsConfig.HOT_RECOMMEND_LEVEL, partHotNewsList.size());
        // 根据随机策略获取应该选取的下标
        List<Integer> indexList = RandomUtils.randomIndexWithAvgStrategy(partHotNewsList.size(), hotCount);
        List<Double> commentCountList = new ArrayList<>();
        for (int nowIndex : indexList) {
            hotNewsList.add(partHotNewsList.get(nowIndex).getValue());
            commentCountList.add(partHotNewsList.get(nowIndex).getScore());
        }
        logger.info("热门推荐完成, 得出推荐文章:{}篇, 具体为:{}, 对应评论数目为:{}", hotNewsList.size(), hotNewsList, commentCountList);

        return hotNewsList;
    }

    public List<String> getRandomNewsList(User user, String cat, int randomCount) {

        logger.info("开始随机推荐...");
        List<String> randomNewsList = new ArrayList<>();

        // cat - scan = 可推送的新闻集合
        Set<ZSetOperations.TypedTuple<String>> unScanNews = getUnScanNews(user, cat);
        List<ZSetOperations.TypedTuple<String>> unScanNewsList = unScanNews.stream().collect(Collectors.toList());
        logger.info("可以推送的新闻数目为:{}", unScanNews.size());

        // 根据随机策略获取应该选取的下标
        List<Integer> indexList = RandomUtils.randomIndexWithAvgStrategy(unScanNews.size(), randomCount);
        for (int nowIndex : indexList) {
            randomNewsList.add(unScanNewsList.get(nowIndex).getValue());
        }
        logger.info("随机推荐完成, 得出推荐文章:{}篇, 具体为:{}", randomNewsList.size(), randomNewsList);

        return randomNewsList;

    }

    public List<News> getNewsListDetail(List<String> newsList, int source) {
        if (newsList == null || newsList.size() == 0) return new ArrayList<>();
        List<News> result = newsMapper.getNewsByIds(newsList);
        for (int i = 0; i < result.size(); i++) {
            result.get(i).setRecommendSource(source);
        }
        return result;
    }

    public NewsDetail getNewsDetail(String docId) {
        return newsMapper.getNewsDetailById(docId);
    }

    public void saveClickAction(User user, String docId) {

        // 存储点击记录到数据库
        userMapper.saveUserAction(user, docId, 0);

        // 存储点击记录到redis
        redisUtils.addKeyToSet(NConstants.NewsConfig.USER_VIEW_NEWS_PREFIX + user.getOpenid(), docId);

        // 更新用户-主题 评分
        Set<ZSetOperations.TypedTuple<String>> newsTopicSet =
                redisUtils.getZSetByRank(NConstants.NewsConfig.NEWS_TOPIC_PREFIX + docId, 0, -1);
        for (ZSetOperations.TypedTuple<String> keyValue : newsTopicSet) {
            redisUtils.addZSetScore(NConstants.NewsConfig.USER_TOPIC_PREFIX + user.getOpenid(),
                    keyValue.getValue(), keyValue.getScore());
        }
        logger.info("存储点击事件成功, user:{}, docId:{}", user, docId);

    }


    public String test() {

        logger.info("done!");
        return "hello";
    }


}
