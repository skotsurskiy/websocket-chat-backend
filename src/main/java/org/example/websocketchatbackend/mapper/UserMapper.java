package org.example.websocketchatbackend.mapper;

import org.example.websocketchatbackend.config.MapperConfig;
import org.example.websocketchatbackend.dto.user.UpdateUserResponseDto;
import org.example.websocketchatbackend.dto.user.UserRegisterRequestDto;
import org.example.websocketchatbackend.dto.user.UserResponseDto;
import org.example.websocketchatbackend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
  User toUserEntity(UserRegisterRequestDto requestDto);

  UserResponseDto toUserResponseDto(User user);

  @Mapping(target = "token", ignore = true)
  UpdateUserResponseDto toUpdateUserResponseDto(User user);
}
