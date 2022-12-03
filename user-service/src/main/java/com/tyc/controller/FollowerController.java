package com.tyc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tyc.constant.ApiUrls.FOLLOWER;

@RestController
@RequestMapping(FOLLOWER)
@RequiredArgsConstructor
public class FollowerController {
}
