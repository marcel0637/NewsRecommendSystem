package top.mar.recommend.mapper;

import org.apache.ibatis.annotations.*;
import top.mar.recommend.model.User;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select openid from user where openid=#{openid}")
    User getUserById(String openid);

    @Insert({"insert into user(openid) values(#{openid})"})
    void saveUser(User user);

    @Insert({"insert into user_news(user_id, news_id, action) values(#{user.openid},#{docId},#{action})"})
    void saveUserAction(User user, String docId, int action);

    @Select("select user_id from user_news where user_id=#{user.openid} and action=1 and news_id=#{docId}")
    String isUserCollect(User user, String docId);

    @Delete("delete from user_news where user_id=#{user.openid} and action = #{action} and news_id=#{docId}")
    void deleteUserAction(User user, String docId, int action);

    @Select("select news_id from user_news where user_id=#{user.openid} and action = #{action}")
    List<String> getUserActionList(User user,int action);
}
