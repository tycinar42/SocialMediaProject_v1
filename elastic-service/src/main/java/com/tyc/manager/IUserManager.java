package com.tyc.manager;

import com.tyc.dto.response.UserProfileResponseDto;
import com.tyc.repository.entity.UserProfile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static com.tyc.constant.ApiUrls.*;

@FeignClient(url = "${myapplication.feign.user}/user",name = "user-service-userprofile",decode404 = true)
public interface IUserManager {


    @GetMapping(GETALL)
    public ResponseEntity<List<UserProfileResponseDto>> findAll();
}
