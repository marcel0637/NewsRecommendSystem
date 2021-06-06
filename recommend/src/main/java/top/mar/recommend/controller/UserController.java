package top.mar.recommend.controller;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import top.mar.recommend.consts.NConstants;
import top.mar.recommend.model.User;
import top.mar.recommend.service.UserService;
import top.mar.recommend.utils.HttpUtil;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> mini_Login(@Param("code") String code) {
        Map<String, Object> res = new HashMap<>();//这里是自定义类，用于封装返回的数据，你可以用map替代
        String result = "";
        try {//请求微信服务器，用code换取openid。HttpUtil是工具类，后面会给出实现，Configure类是小程序配置信息，后面会给出代码
            result = HttpUtil.doGet(
                    "https://api.weixin.qq.com/sns/jscode2session?appid="
                            + NConstants.WXConfig.MINI_APP_ID + "&secret="
                            + NConstants.WXConfig.MINI_SECRET + "&js_code="
                            + code
                            + "&grant_type=" + NConstants.WXConfig.GRANT_TYPE, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("login code:{}, weixin api response:{}", code, result);
        //解析从微信服务器上获取到的json字符串
        JSONObject jsonObj = JSONObject.fromObject(result);
        String openid = jsonObj.get("openid").toString();
        res.put("session_key", jsonObj.get("session_key").toString());
        User user = userService.getUserById(openid);
        res.put("userid", openid);
        // 已经存在的用户就直接返回
        if (user != null) {
            return res;
        }
        user = new User(openid);
        // 不存在的用户进行保存
        userService.saveUser(user);
        return res;
    }
}
