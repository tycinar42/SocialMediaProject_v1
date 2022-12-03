package com.tyc.controller;

import com.tyc.dto.request.CreateCommentDto;
import com.tyc.exception.ErrorType;
import com.tyc.exception.PostManagerException;
import com.tyc.repository.entity.Comment;
import com.tyc.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.tyc.constant.ApiUrls.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(COMMENT)
public class CommentController {


    private final CommentService commentService;

    @PostMapping(CREATE)
    public ResponseEntity<Boolean> createComment(@RequestBody @Valid CreateCommentDto dto) {
        try {
            commentService.create(dto);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            throw new PostManagerException(ErrorType.COMMENT_NOT_CREATED);
        }

    }

    @GetMapping(GETALL)
    public ResponseEntity<List<Comment>> findall() {

        return ResponseEntity.ok(commentService.findAll());
    }

}
