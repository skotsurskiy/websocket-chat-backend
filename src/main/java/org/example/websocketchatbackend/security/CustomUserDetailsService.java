package org.example.websocketchatbackend.security;

import lombok.RequiredArgsConstructor;
import org.example.websocketchatbackend.exception.UserNotFoundException;
import org.example.websocketchatbackend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername((username)).orElseThrow(()
        -> new UserNotFoundException(username));
  }
}
