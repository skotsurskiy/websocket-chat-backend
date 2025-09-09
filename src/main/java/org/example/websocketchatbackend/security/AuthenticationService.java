package org.example.websocketchatbackend.security;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.websocketchatbackend.dto.user.UserLoginRequestDto;
import org.example.websocketchatbackend.dto.user.UserRegisterRequestDto;
import org.example.websocketchatbackend.dto.user.UserTokenResponseDto;
import org.example.websocketchatbackend.exception.UserNameAlreadyExistsException;
import org.example.websocketchatbackend.mapper.UserMapper;
import org.example.websocketchatbackend.model.Role;
import org.example.websocketchatbackend.model.User;
import org.example.websocketchatbackend.repository.RoleRepository;
import org.example.websocketchatbackend.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationService {
  private final JwtUtil jwtUtil;
  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;

  public UserTokenResponseDto authenticate(UserLoginRequestDto requestDto) {
    Authentication authenticate = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(requestDto.username(),
            requestDto.password())
    );
    return new UserTokenResponseDto(jwtUtil.generateToken(authenticate.getName()));
  }

  public UserTokenResponseDto register(UserRegisterRequestDto requestDto) {
    if (userRepository.existsByUsername(requestDto.username())) {
      throw new UserNameAlreadyExistsException(requestDto.username());
    }
    User userEntity = userMapper.toUserEntity(requestDto);
    userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
    userEntity.setRoles(Set.of(roleRepository.findRoleByName(Role.RoleName.ROLE_USER)));
    userRepository.save(userEntity);
    return new UserTokenResponseDto(jwtUtil.generateToken(userEntity.getUsername()));
  }
}
