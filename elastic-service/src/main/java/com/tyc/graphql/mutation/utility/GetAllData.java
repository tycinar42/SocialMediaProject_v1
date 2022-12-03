package com.tyc.graphql.mutation.utility;

import com.tyc.dto.response.UserProfileResponseDto;
import com.tyc.manager.IUserManager;
import com.tyc.mapper.IUserMapper;
import com.tyc.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetAllData {


    private final UserProfileService userProfileService;

    private final IUserManager userManager;

    //@PostConstruct
    public void init() {

        List<UserProfileResponseDto> userProfileList = userManager.findAll().getBody();


        userProfileService.saveAll(userProfileList.stream().
                map(dto -> IUserMapper.INSTANCE.toUserProfile(dto))
                .collect(Collectors.toList()));

    }

}
