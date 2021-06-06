package top.mar.recommend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.mar.recommend.consts.NConstants;
import top.mar.recommend.model.News;
import top.mar.recommend.model.NewsDetail;
import top.mar.recommend.model.User;
import top.mar.recommend.service.NewsService;
import top.mar.recommend.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
public class NewsController {

    private static Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @GetMapping("/test")
    String test() {
        return newsService.test();
    }


    @GetMapping("/newsList/{cat}")
    List<News> getNewsList(HttpServletRequest request, @PathVariable String cat) {
        long startTime = System.currentTimeMillis();
        User user = (User) request.getAttribute("user");
        logger.info("*****************************************");
        logger.info("开始本次新闻推荐，user:{}, cat:{}", user, cat);

        int calCount = NConstants.NewsConfig.CAL_COUNT;
        int hotCount = NConstants.NewsConfig.HOT_COUNT;
        int randomCount = NConstants.NewsConfig.RANDOM_COUNT;
        if (cat.equals("hot")) {
            hotCount += calCount + randomCount;
            calCount = 0;
            randomCount = 0;
        } else if (newsService.getViewCount(user) <= 0) {
            hotCount += calCount; // 用户没有看过文章的情况下，推荐热门文章
            calCount = 0;
        }
        logger.info("各种类型的期望推荐数量为 calCount:{}, hotCount:{}, randomCount:{}", calCount, hotCount, randomCount);

        List<News> result = new ArrayList<>();

        if (calCount > 0) { // 算法推荐
            List<String> roughNewsList = newsService.getRoughNewsList(user, cat);
            List<String> carefulNewsList = newsService.getCarefulNewsList(user, roughNewsList, calCount);
            newsService.markAsScanNews(user, carefulNewsList);
            result.addAll(newsService.getNewsListDetail(carefulNewsList, 0));
        }

        if (hotCount > 0) { // 热门推荐
            // 纯热门推荐，需要设置cal为all
            List<String> hotNewsList = newsService.getHotNewsList(user, randomCount == 0 ? "all" : cat, hotCount);
            newsService.markAsScanNews(user, hotNewsList);
            result.addAll(newsService.getNewsListDetail(hotNewsList, 1));
        }

        if (randomCount > 0) { // 随机推荐
            List<String> randomNewsList = newsService.getRandomNewsList(user, cat, randomCount);
            newsService.markAsScanNews(user, randomNewsList);
            result.addAll(newsService.getNewsListDetail(randomNewsList, 2));
        }

        // 按时间降序排列
        result.sort((o1, o2) -> ((o2.getNewsTimeStamp() - o1.getNewsTimeStamp()) > 0L ? 1 : -1));
        double spendTime = (System.currentTimeMillis() - startTime) / 1000.0;
        logger.info("本次新闻推荐完成，耗时:{}s, 推荐新闻:{}篇, 具体为:{}", spendTime, result.size(), result);
        logger.info("*****************************************");
        return result;
    }

    @GetMapping("/news/{docId}")
    public NewsDetail getNewsDetail(@PathVariable String docId) {
        return newsService.getNewsDetail(docId);
    }

    @PostMapping("/news/{docId}")
    public void doAction(HttpServletRequest request, @PathVariable String docId) {
        User user = (User) request.getAttribute("user");
        newsService.saveClickAction(user, docId);
    }

    @GetMapping("/news/collect/{docId}")
    public boolean isUserCollect(HttpServletRequest request, @PathVariable String docId) {
        User user = (User) request.getAttribute("user");
        return userService.isUserCollect(user, docId);
    }

    @PostMapping("/news/collect/{docId}")
    public void collect(HttpServletRequest request, @PathVariable String docId) {
        User user = (User) request.getAttribute("user");
        if (userService.isUserCollect(user, docId)) {
            userService.unCollect(user, docId);
            logger.info("user:{} unCollect docId:{} success", user, docId);
        } else {
            userService.collect(user, docId);
            logger.info("user:{} collect docId:{} success", user, docId);
        }
    }

    @GetMapping("/collectList")
    public List<News> getCollectList(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        List<News> res = userService.getCollectList(user);
        logger.info("user:{} getCollectList:{}", user, res);
        return res;
    }

    @GetMapping("/viewList")
    public List<News> getViewList(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        List<News> res = userService.getViewList(user);
        logger.info("user:{} getViewList:{}", user, res);
        return res;
    }

    @GetMapping("/interestWords/{maxCount}")
    public List<String> getInterestWords(HttpServletRequest request, @PathVariable int maxCount) {
        User user = (User) request.getAttribute("user");
        List<String> res = userService.getInterestWords(user, maxCount);
        logger.info("user:{} getInterestWords:{}", user, res);
        return res;
    }

    @GetMapping("/news/search/{key}")
    public List<News> search(@PathVariable String key) {
        List<News> res = userService.search(key);
        logger.info("search:{} getResList:{}", key, res);
        return res;
    }
}
