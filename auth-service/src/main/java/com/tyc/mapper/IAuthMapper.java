package com.tyc.mapper;

import com.tyc.dto.request.LoginRequestDto;
import com.tyc.dto.request.NewCreateUserDto;
import com.tyc.dto.request.RegisterRequestDto;
import com.tyc.dto.response.AuthListResponseDto;
import com.tyc.dto.response.LoginResponseDto;
import com.tyc.dto.response.RegisterResponseDto;
import com.tyc.dto.response.RoleResponseDto;
import com.tyc.repository.entity.Auth;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAuthMapper {

    IAuthMapper INSTANCE= Mappers.getMapper(IAuthMapper.class);
    RegisterRequestDto toRegisterRequestDto(final Auth auth);
    Auth toAuth(final RegisterRequestDto dto);

    Auth toAuth(final  LoginRequestDto dto);

    LoginResponseDto toLoginResponseDto(final Auth auth);

    RegisterResponseDto toRegisterResponseDto(final Auth auth);

    NewCreateUserDto toNewCreateUserDto(final  Auth auth);

    RoleResponseDto toRoleResponseDto(final  Auth auth);

    List<AuthListResponseDto> tAuthListResponseDtoList(final List<Auth> auths);


}
