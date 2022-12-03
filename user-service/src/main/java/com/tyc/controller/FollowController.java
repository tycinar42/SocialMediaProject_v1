package com.tyc.controller;

import com.tyc.dto.request.DeleteFollowDto;
import com.tyc.dto.request.FindByToken;
import com.tyc.dto.request.FollowCreateDto;
import com.tyc.dto.response.UserProfilePostResponseDto;
import com.tyc.repository.entity.Follow;
import com.tyc.repository.entity.UserProfile;
import com.tyc.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.tyc.constant.ApiUrls.CREATE;
import static com.tyc.constant.ApiUrls.FOLLOW;

@RestController
@RequestMapping(FOLLOW)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FollowController {

    private final FollowService followService;

    @PostMapping(CREATE)
    public ResponseEntity<Boolean> createFollow(@RequestBody FollowCreateDto dto) {
        followService.create(dto);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteFollow(@RequestBody DeleteFollowDto dto) {


        return ResponseEntity.ok(followService.deleteFollow(dto));

    }

    @PostMapping("/findfollows")
    public ResponseEntity<List<UserProfile>> findFollowsById(@RequestBody FindByToken token) {

        return ResponseEntity.ok(followService.findFollowById(token.getToken(), token.getId()));
    }

    @PostMapping("/findfollowsbytoken")
    public ResponseEntity<List<Follow>> findFollowsByToken(@RequestBody FindByToken token) {

        return ResponseEntity.ok(followService.findFollowByToken(token.getToken()));
    }

    @GetMapping("/findall")
    public ResponseEntity<List<Follow>> findall() {

        return ResponseEntity.ok(followService.findAll());
    }

    @PostMapping("/findmyfollows")
    public ResponseEntity<List<UserProfilePostResponseDto>> findMyFollowPost(@RequestBody FindByToken mytoken) {

        return ResponseEntity.ok(followService.findMyFollowPost(mytoken));
    }
}
