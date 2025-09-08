package org.example.websocketchatbackend.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.websocketchatbackend.dto.user.UpdateUserResponseDto;
import org.example.websocketchatbackend.dto.user.UserResponseDto;
import org.example.websocketchatbackend.exception.UserNameAlreadyExistsException;
import org.example.websocketchatbackend.exception.UserNotFoundException;
import org.example.websocketchatbackend.mapper.UserMapper;
import org.example.websocketchatbackend.model.User;
import org.example.websocketchatbackend.repository.UserRepository;
import org.example.websocketchatbackend.security.JwtUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private static final String URL_IMAGES = "src/main/webapp/WEB-INF/images";
  private static final String URL_WEBINF = "src/main/webapp/WEB-INF";
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final JwtUtil jwtUtil;

  @Override
  public List<UserResponseDto> findByUsername(String username) {
    return userRepository.findByUsernameContainingIgnoreCase(username).stream()
        .map(userMapper::toUserResponseDto)
        .toList();
  }

  @Override
  public UserResponseDto getProfile() {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userMapper.toUserResponseDto(userRepository.findByUsername(user.getUsername())
        .orElseThrow(() -> new UserNotFoundException(user.getUsername())));
  }

  @Override
  public UpdateUserResponseDto updateProfile(String username, MultipartFile file) {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String token = null;

    if (userRepository.existsByUsername(username)) {
      throw new UserNameAlreadyExistsException(username);
    }

    if (file != null) {
      deleteLastImage(user.getAvatarUrl());
      user.setAvatarUrl(saveAvatar(file));
    }

    if (username != null) {
      user.setUsername(username);
      token = jwtUtil.generateToken(username);
    }

    UpdateUserResponseDto updateUserResponseDto =
        userMapper.toUpdateUserResponseDto(userRepository.save(user));

    if (token != null) {
      updateUserResponseDto.setToken(token);
    }

    return updateUserResponseDto;
  }

  private void deleteLastImage(String fileUrl) {
    if (fileUrl == null || fileUrl.isBlank()) {
      return;
    }

    try {
      String filename = Paths.get(URL_WEBINF + fileUrl).getFileName().toString();

      Path filePath = Paths.get(URL_IMAGES, filename);
      Files.deleteIfExists(filePath);

      System.out.println("Файл видалено: " + filePath);
    } catch (IOException e) {
      throw new RuntimeException("Помилка при видаленні файла: " + fileUrl, e);
    }
  }

  private String saveAvatar(MultipartFile file) {
    if (file.isEmpty()) {
      throw new IllegalArgumentException("Файл пустий!");
    }

    try {
      File uploadDir = new File(URL_IMAGES);
      if (!uploadDir.exists()) {
        uploadDir.mkdirs();
      }

      String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
      Path filePath = Paths.get(URL_IMAGES, filename);

      Files.write(filePath, file.getBytes());

      return "/images/" + filename;
    } catch (IOException e) {
      throw new RuntimeException("Помилка при збереженні файла", e);
    }
  }
}
