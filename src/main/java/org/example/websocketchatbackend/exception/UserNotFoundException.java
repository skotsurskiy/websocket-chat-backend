package org.example.websocketchatbackend.exception;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String username) {
    super("Account with username '%s' doesnt exist ".formatted(username));
  }
}
