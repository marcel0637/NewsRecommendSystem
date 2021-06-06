package top.mar.recommend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.mar.recommend.model.Comment;
import top.mar.recommend.service.CommentService;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

@RestController
public class CommentController {

    private static Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    CommentService commentService;

    @PostMapping("/comment")
    public void saveComment(@RequestBody Comment comment) {
        comment.setCreateTime(new Date());
        commentService.saveComment(comment);
        logger.info("saveComment success, comment:{}", comment);
    }

    @GetMapping("/commentList/{docId}")
    public List<Comment> getCommentsById(@PathVariable String docId) {
        List<Comment> res = commentService.getCommentsById(docId);
        logger.info("getCommentsById success, docId:{}, commentList:{}", docId, res);
        return res;
    }
}
