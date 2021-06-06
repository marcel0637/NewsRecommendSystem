package top.mar.recommend.mapper;

import org.apache.ibatis.annotations.*;
import top.mar.recommend.model.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Insert({"insert into comment(docid,content,avatar,nick_name) values(#{docId},#{content},#{avatar},#{nickName})"})
    void saveComment(Comment comment);

    @Select("select * from comment where docid=#{docId}")
    @Results(id = "commentMap", value = {
            @Result(property = "docId", column = "docid"),
            @Result(property = "content", column = "content"),
            @Result(property = "avatar", column = "avatar"),
            @Result(property = "nickName", column = "nick_name"),
            @Result(property = "createTime", column = "create_time"),
    })
    List<Comment> getCommentsById(String docId);
}
