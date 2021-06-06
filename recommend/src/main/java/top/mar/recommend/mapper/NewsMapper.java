package top.mar.recommend.mapper;

import org.apache.ibatis.annotations.*;
import top.mar.recommend.model.News;
import top.mar.recommend.model.NewsDetail;

import java.util.List;

@Mapper
public interface NewsMapper {

    @Select({
            "<script>",
            "select *",
            "from news",
            "where docid in",
            "<foreach collection='ids' item='docid' open='(' separator=',' close=')'>",
            "#{docid}",
            "</foreach>",
            "order by newstime",
            "</script>"
    })
    @Results(id = "newsMap", value = {
            @Result(property = "docId", column = "docid"),
            @Result(property = "title", column = "title"),
            @Result(property = "cat", column = "cat"),
            @Result(property = "commentCount", column = "comment_count"),
            @Result(property = "newsTime", column = "newstime"),
            @Result(property = "url", column = "url"),
            @Result(property = "img", column = "img"),
    })
    List<News> getNewsByIds(@Param("ids") List<String> ids);

    @Select({"select * from news where docid = #{id}"})
    @Results(id = "newsDetailMap", value = {
            @Result(property = "docId", column = "docid"),
            @Result(property = "title", column = "title"),
            @Result(property = "artibody", column = "content"),
            @Result(property = "tag", column = "cat"),
            @Result(property = "commentCount", column = "comment_count"),
            @Result(property = "newsTime", column = "newstime"),
            @Result(property = "newsFrom", column = "newsFrom"),
            @Result(property = "url", column = "url"),
            @Result(property = "img", column = "img"),
    })
    NewsDetail getNewsDetailById(@Param("id") String id);

    @Select({"select * from news where title like \"%\"#{key}\"%\" order by newstime desc"})
    @Results(id = "newsMap2", value = {
            @Result(property = "docId", column = "docid"),
            @Result(property = "title", column = "title"),
            @Result(property = "cat", column = "cat"),
            @Result(property = "commentCount", column = "comment_count"),
            @Result(property = "newsTime", column = "newstime"),
            @Result(property = "url", column = "url"),
            @Result(property = "img", column = "img"),
    })
    List<News> search(@Param("key") String key);
}
