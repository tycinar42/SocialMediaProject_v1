package com.tyc.controller;

import com.tyc.dto.request.ActivateReguestDto;
import com.tyc.dto.request.FindByToken;
import com.tyc.dto.request.NewCreateUserDto;
import com.tyc.dto.request.UpdateRequestDto;
import com.tyc.dto.response.RoleResponseDto;
import com.tyc.dto.response.UserProfilePostResponseDto;
import com.tyc.dto.response.UserProfileRedisResponseDto;
import com.tyc.dto.response.UserProfileResponseDto;
import com.tyc.exception.ErrorType;
import com.tyc.exception.UserManagerException;
import com.tyc.mapper.IUserMapper;
import com.tyc.repository.entity.UserProfile;
import com.tyc.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.tyc.constant.ApiUrls.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(USER)
@CrossOrigin(origins = "*")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping(CREATE)
    public ResponseEntity<Boolean> createUser(@RequestBody NewCreateUserDto dto) {

        try {
            System.out.println(dto);
            userProfileService.createUser(dto);
            System.out.println(dto);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            throw new UserManagerException(ErrorType.USER_NOT_CREATED);
        }

    }

    @PostMapping(ACTIVATESTATUS)
    public ResponseEntity<Boolean> activateStatus(@RequestBody ActivateReguestDto dto) {

        return ResponseEntity.ok(userProfileService.activateStatus(dto));

    }

    @PostMapping(ACTIVATESTATUSBYID)
    public ResponseEntity<Boolean> activateStatus(@PathVariable Long authid) {

        return ResponseEntity.ok(userProfileService.activateStatus(authid));

    }

    @PutMapping(UPDATE)
    public ResponseEntity<Boolean> updateProfile(@RequestBody @Valid UpdateRequestDto dto) {


        return ResponseEntity.ok(userProfileService.updateUser(dto));
    }

    @PutMapping("/updateredis")
    public ResponseEntity<Boolean> updateProfileForRedis(@RequestBody @Valid UpdateRequestDto dto) {


        return ResponseEntity.ok(userProfileService.updateUser(dto));
    }

    @PutMapping("/updatewithrabbitmq")
    public ResponseEntity<Boolean> updateProfileWithRabbitmq(@RequestBody UpdateRequestDto dto) {
        return ResponseEntity.ok(userProfileService.updateUserWithRabbitMq(dto));
    }

    @GetMapping(GETALL)

    public ResponseEntity<List<UserProfileResponseDto>> findAll() {

        return ResponseEntity.ok(IUserMapper.INSTANCE.toUserProfileResponseDtoList(userProfileService.findAll()));
    }

    @GetMapping("/findbyusername/{username}")
    public ResponseEntity<UserProfileRedisResponseDto> findbyUsername(@PathVariable String username) {


        return ResponseEntity.ok(userProfileService.findByUsername(username));

    }

    @GetMapping("/findallactiveprofile")
    public ResponseEntity<List<UserProfile>> findAllActiveProfile() {

        return ResponseEntity.ok(userProfileService.findAllActiveProfile());

    }


    @GetMapping("/findbyrole/{roles}")
    public ResponseEntity<List<RoleResponseDto>> findAllByRole(@PathVariable String roles) {

        return ResponseEntity.ok(userProfileService.findByRole(roles));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(userProfileService.deleteUser(id));
    }


    @GetMapping("/findbypagable")
    public ResponseEntity<Page<UserProfile>> findAllPAge(int pageSize, int pageNumber, String direction, String sortParameter) {
        return ResponseEntity.ok(userProfileService.findallPage(pageSize, pageNumber, direction, sortParameter));
    }

    @GetMapping("/findbyslice")
    public ResponseEntity<Slice<UserProfile>> findAllSlice(int pageSize, int pageNumber, String direction, String sortParameter) {
        return ResponseEntity.ok(userProfileService.findallSlice(pageSize, pageNumber, direction, sortParameter));
    }


    @GetMapping("/findbyautid/{id}")
    public ResponseEntity<UserProfilePostResponseDto> findbyAuthId(@PathVariable Long id) {

        UserProfilePostResponseDto userProfilePostResponseDto =
                IUserMapper.INSTANCE.toUserProfilePostResponseDto(userProfileService.findByAuthId(id).get());
        return ResponseEntity.ok(userProfilePostResponseDto);
    }

    @PostMapping("/findbyid/{id}")
    public ResponseEntity<UserProfile> findbyId(@PathVariable String id, @RequestBody FindByToken token) {

        return ResponseEntity.ok(userProfileService.findById(id, token));
    }

    @PostMapping("/finduserbyid/{id}")
    public ResponseEntity<UserProfile> findbyId(@PathVariable String id) {

        return ResponseEntity.ok(userProfileService.findById(id).get());
    }

    @PostMapping("/findbytoken")
    public ResponseEntity<UserProfile> findbyToken(@RequestBody FindByToken token) {
       
        return ResponseEntity.ok(userProfileService.findByToken(token.getToken()));
    }
}
