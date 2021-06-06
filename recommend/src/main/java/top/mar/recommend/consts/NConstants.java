package top.mar.recommend.consts;

public interface NConstants {

    // 微信相关配置
    class WXConfig {
//        public static String MINI_APP_ID = "wx19b089d29128cb0c";
        public static String MINI_APP_ID = "wx9c42a16a9288567a";
//        public static String MINI_SECRET = "5c5dc22baa501706e3e02c339597b81f";
        public static String MINI_SECRET = "3a1f5912b8439e504e6aed1faeff3d39";
        public static String GRANT_TYPE = "authorization_code";
    }

    // 新闻相关配置
    class NewsConfig {
        // 粗排筛选出来的数目
        public static int ROUGH_RECOMMEND_COUNT = 100;
        // 7个推荐，3个热门，2个随机
        public static int TOTAL_RECOMMEND_COUNT = 12;
        public static int CAL_COUNT = 7;
        public static int HOT_COUNT = 3;
        public static int RANDOM_COUNT = 2;
        public static int ROUGH_TOPIC_COUNT = 20;
        public static int HOT_RECOMMEND_LEVEL = 5; // 只推荐评论大于等于这么多的热点新闻

        public static String NEWS_TOPIC_PREFIX = "news:topic::";
        public static String TOPIC_NEWS_PREFIX = "topic:news::";
        public static String CAT_NEWS_PREFIX = "cat:news::";
        public static String USER_VIEW_NEWS_PREFIX = "user:view:news::";
        public static String USER_SCAN_NEWS_PREFIX = "user:scan:news::";
        public static String USER_TOPIC_PREFIX = "user:topic::";
        public static String TOPIC_WORD_PREFIX = "topic:word::";
        public static String TMP_USER_UN_SCAN_NEWS_PREFIX = "tmp:user:unscan:news::";
        public static String TMP_UN_SCAN_INTER_TOPIC_NEWS_PREFIX = "tmp:user:unscan:inter:topic:news::";
    }

    // 文件相关配置
    class FileConfig {
        public static String RESULT_FILE_PATH = "/Users/yanghao/Desktop/local-env/Python/TouTiaoNewsScraper/res/";
    }

}
