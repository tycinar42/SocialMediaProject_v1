package com.tyc.manager;

import com.tyc.dto.response.UserProfileResponseDto;
import com.tyc.repository.entity.UserProfile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.tyc.constant.ApiUrls.CREATE;
import static com.tyc.constant.ApiUrls.UPDATE;

@FeignClient(url ="${myapplication.feign.elastic}/elastic" , name = "elastic-service",decode404 = true)
public interface IElasticManager {

    @PostMapping(CREATE)
    public ResponseEntity<UserProfile> createUser(@RequestBody UserProfileResponseDto userProfileResponseDto);

    @PutMapping(UPDATE)
    public ResponseEntity<UserProfile> update(@RequestBody UserProfileResponseDto userProfileResponseDto);


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id);
}
