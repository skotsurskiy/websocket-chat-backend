package org.example.websocketchatbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.websocketchatbackend.dto.user.UserLoginRequestDto;
import org.example.websocketchatbackend.dto.user.UserLoginResponseDto;
import org.example.websocketchatbackend.dto.user.UserRegisterRequestDto;
import org.example.websocketchatbackend.dto.user.UserResponseDto;
import org.example.websocketchatbackend.security.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthenticationService authenticationService;

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public UserLoginResponseDto authenticate(@RequestBody @Valid UserLoginRequestDto requestDto) {
    return authenticationService.authenticate(requestDto);
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.OK)
  public UserResponseDto register(@RequestBody @Valid UserRegisterRequestDto requestDto) {
    return authenticationService.register(requestDto);
  }
}
