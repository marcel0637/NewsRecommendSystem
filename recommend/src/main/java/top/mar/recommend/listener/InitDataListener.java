package top.mar.recommend.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;
import top.mar.recommend.consts.NConstants;
import top.mar.recommend.service.NewsService;
import top.mar.recommend.utils.RedisUtils;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Set;

@Service
public class InitDataListener implements InitializingBean, ServletContextAware {

    private static Logger logger = LoggerFactory.getLogger(InitDataListener.class);

    @Autowired
    RedisUtils redisUtils;

    // 新闻-主题-权重
    public static HashMap<String, Set<ZSetOperations.TypedTuple<String>>> newsTopicGlobalMap;
    // 主题-单词-权重
    public static HashMap<String, Set<ZSetOperations.TypedTuple<String>>> topicWordGlobalMap;
    // 主题-新闻-权重
    public static HashMap<String, Set<ZSetOperations.TypedTuple<String>>> topicNewsGlobalMap;


    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        Set<String> newsTopicKeys = redisUtils.getAllKeys(NConstants.NewsConfig.NEWS_TOPIC_PREFIX + "*");
        newsTopicGlobalMap = new HashMap<>();
        for (String key : newsTopicKeys) {
            newsTopicGlobalMap.put(key, redisUtils.getZSetByRank(key, 0, -1));
        }
        logger.info("从redis初始化加载新闻-主题数据到map成功...");

        Set<String> topicWordKeys = redisUtils.getAllKeys(NConstants.NewsConfig.TOPIC_WORD_PREFIX + "*");
        topicWordGlobalMap = new HashMap<>();
        for (String key : topicWordKeys) {
            topicWordGlobalMap.put(key, redisUtils.getZSetByRank(key, 0, -1));
        }
        logger.info("从redis初始化加载主题-单词数据到map成功...");

        Set<String> topicNewsKeys = redisUtils.getAllKeys(NConstants.NewsConfig.TOPIC_NEWS_PREFIX + "*");
        topicNewsGlobalMap = new HashMap<>();
        for (String key : topicNewsKeys) {
            topicNewsGlobalMap.put(key, redisUtils.getZSetByRank(key, 0, -1));
        }
        logger.info("从redis初始化加载主题-新闻数据到map成功...");
    }
}
