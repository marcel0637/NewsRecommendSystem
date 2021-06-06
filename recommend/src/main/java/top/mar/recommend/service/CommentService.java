package top.mar.recommend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.mar.recommend.mapper.CommentMapper;
import top.mar.recommend.model.Comment;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentMapper commentMapper;

    public void saveComment(Comment comment) {
        commentMapper.saveComment(comment);
    }

    public List<Comment> getCommentsById(String docId) {
        return commentMapper.getCommentsById(docId);
    }
}
