package com.tyc.mapper;

import com.tyc.dto.request.NewCreateUserDto;
import com.tyc.dto.request.UpdateRequestDto;
import com.tyc.dto.response.UserProfilePostResponseDto;
import com.tyc.dto.response.UserProfileRedisResponseDto;
import com.tyc.dto.response.UserProfileResponseDto;
import com.tyc.repository.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface IUserMapper {


    IUserMapper INSTANCE = Mappers.getMapper(IUserMapper.class);


    UserProfile toUserProfile(final NewCreateUserDto dto);


    UserProfile toUserProfile(final UpdateRequestDto dto);

    UserProfileRedisResponseDto toUserProfileRedisResponseDto(final UserProfile userProfile);

    UserProfileResponseDto toUserProfileResponseDto(final UserProfile userProfile);

    List<UserProfileResponseDto> toUserProfileResponseDtoList(final List<UserProfile> userProfile);


    UserProfilePostResponseDto toUserProfilePostResponseDto(final UserProfile userProfile);

}
