package top.mar.recommend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import top.mar.recommend.consts.NConstants;
import top.mar.recommend.listener.InitDataListener;
import top.mar.recommend.mapper.NewsMapper;
import top.mar.recommend.mapper.UserMapper;
import top.mar.recommend.model.News;
import top.mar.recommend.model.User;
import top.mar.recommend.utils.RedisUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserMapper userMapper;

    @Autowired
    NewsMapper newsMapper;

    @Autowired
    RedisUtils redisUtils;

    public User getUserById(String openid) {
        return userMapper.getUserById(openid);
    }

    public void saveUser(User user) {
        userMapper.saveUser(user);
    }

    public boolean isUserCollect(User user, String docId) {
        String id = userMapper.isUserCollect(user, docId);
        return id != null && !id.equals("");
    }

    public void collect(User user, String docId) {
        userMapper.saveUserAction(user, docId, 1);
    }

    public void unCollect(User user, String docId) {
        userMapper.deleteUserAction(user, docId, 1);
    }

    public List<News> getCollectList(User user) {
        List<String> ids = userMapper.getUserActionList(user, 1);
        if (ids == null || ids.size() == 0) return new LinkedList<>();
        return newsMapper.getNewsByIds(ids);
    }

    public List<News> getViewList(User user) {
        List<String> ids = userMapper.getUserActionList(user, 0);
        if (ids == null || ids.size() == 0) return new LinkedList<>();
        // 浏览记录去重
        ids = ids.stream().distinct().collect(Collectors.toList());
        return newsMapper.getNewsByIds(ids);
    }

    public List<String> getInterestWords(User user, int maxCount) {
        HashMap<String, Double> wordScoreMap = new HashMap<>();
        // 获取用户-主题 map
        Set<ZSetOperations.TypedTuple<String>> userTopicSet =
                redisUtils.getZSetByRank(NConstants.NewsConfig.USER_TOPIC_PREFIX + user.getOpenid(), 0, -1);
        if (userTopicSet == null || userTopicSet.size() == 0) return new LinkedList<>();
        for (ZSetOperations.TypedTuple<String> keyValue : userTopicSet) {
            // 获取主题-单词 map
            Set<ZSetOperations.TypedTuple<String>> topicWordSet =
                    InitDataListener.topicWordGlobalMap.get(NConstants.NewsConfig.TOPIC_WORD_PREFIX + keyValue.getValue());
            for (ZSetOperations.TypedTuple<String> keyValue2 : topicWordSet) {
                double nowScore = keyValue.getScore() * keyValue2.getScore() +
                        wordScoreMap.getOrDefault(keyValue2.getValue(), 0.0);
                wordScoreMap.put(keyValue2.getValue(), nowScore);
            }
        }
        List<Map.Entry<String, Double>> wordScoreList = new ArrayList<>(wordScoreMap.entrySet());
        logger.info("user:{} full interestWords:{}", user, wordScoreList);
        //降序排序
        Collections.sort(wordScoreList, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        List<String> res = new LinkedList<>();
        int targetNum = Math.min(maxCount, wordScoreList.size());
        for (int i = 0; i < wordScoreList.size() && targetNum > 0; i++) {
            String wordKey = wordScoreList.get(i).getKey();
            if (wordKey.length() >= 2) {
                res.add(wordKey);
                targetNum--;
            }
        }
        return res;
    }

    public List<News> search(String key) {
        if (key == null || key.trim().equals("")) return new LinkedList<>();
        List<News> res = newsMapper.search(key);
        if (res == null || res.size() == 0) return new LinkedList<>();
        return res;
    }
}
