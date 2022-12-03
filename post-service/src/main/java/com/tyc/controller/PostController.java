package com.tyc.controller;

import com.tyc.dto.request.*;
import com.tyc.dto.response.*;
import com.tyc.exception.ErrorType;
import com.tyc.exception.PostManagerException;
import com.tyc.repository.entity.Post;
import com.tyc.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.tyc.constant.ApiUrls.*;

@RestController
@RequestMapping(POST)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PostController {


    private final PostService postService;

    @PostMapping(CREATE)
    public ResponseEntity<Boolean> createPost(@RequestBody CreatePostDto dto) {
        try {
            postService.create(dto);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            throw new PostManagerException(ErrorType.POST_NOT_CREATED);
        }
    }

    @GetMapping(GETALL)
    public ResponseEntity<List<Post>> findAll() {
        return ResponseEntity.ok(postService.findAll());
    }


    @PostMapping("/getmypost")
    public ResponseEntity<List<GetAllPost>> getMyPost(@RequestBody FindByToken token) {
        return ResponseEntity.ok(postService.getMyPost(token.getToken()));
    }

    @PostMapping("/getotheruserpost")
    public ResponseEntity<List<GetAllPost>> getMyPost(@RequestBody GetOtherUserPost getOtherUserPost) {


        return ResponseEntity.ok(postService.getOtherUserPost(getOtherUserPost));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deletePost(@RequestBody DeletePostDto deletePostDto) {

        return ResponseEntity.ok(postService.deletePost(deletePostDto));
    }

    @PutMapping(UPDATE)
    public ResponseEntity<Boolean> updatePost(@RequestBody PostUpdateDto postUpdateDto) {

        return ResponseEntity.ok(postService.updatePost(postUpdateDto));
    }

    @PostMapping("/getmyfollowpost")
    public ResponseEntity<List<Post>> myFollowPost(@RequestBody FindByToken token) {
        return ResponseEntity.ok(postService.myFollowPost(token));
    }


}
