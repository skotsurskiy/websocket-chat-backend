package org.example.websocketchatbackend.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.websocketchatbackend.dto.user.UpdateUserResponseDto;
import org.example.websocketchatbackend.dto.user.UserResponseDto;
import org.example.websocketchatbackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping("/find")
  @ResponseStatus(HttpStatus.OK)
  public List<UserResponseDto> findUserByUsername(@RequestParam String username) {
    return userService.findByUsername(username);
  }

  @GetMapping("/profile")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('USER')")
  public UserResponseDto getProfile() {
    return userService.getProfile();
  }

  @PutMapping("/update")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('USER')")
  public UpdateUserResponseDto updateProfile(
      @RequestParam(required = false) String username,
      @RequestParam(required = false) MultipartFile file
  ) {
    return userService.updateProfile(username, file);
  }
}
