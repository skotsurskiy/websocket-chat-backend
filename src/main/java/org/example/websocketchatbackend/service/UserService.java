package org.example.websocketchatbackend.service;

import java.util.List;
import org.example.websocketchatbackend.dto.user.UpdateUserResponseDto;
import org.example.websocketchatbackend.dto.user.UserResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  List<UserResponseDto> findByUsername(String username);

  UserResponseDto getProfile();

  UpdateUserResponseDto updateProfile(String username, MultipartFile file);
}
