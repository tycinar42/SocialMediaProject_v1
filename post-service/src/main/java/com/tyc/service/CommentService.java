package com.tyc.service;

import com.tyc.dto.request.CreateCommentDto;
import com.tyc.mapper.ICommentMapper;
import com.tyc.repository.ICommentRepository;
import com.tyc.repository.entity.Comment;
import com.tyc.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService extends ServiceManager<Comment, String> {


    private final ICommentRepository commentRepository;


    public CommentService(ICommentRepository commentRepository) {
        super(commentRepository);
        this.commentRepository = commentRepository;
    }

    public Comment create(CreateCommentDto dto) {
        return save(ICommentMapper.INSTANCE.toComment(dto));
    }


    public Optional<List<Comment>> findAllByPostId(String id) {
        return commentRepository.findAllOptionalByPostId(id);
    }
}
