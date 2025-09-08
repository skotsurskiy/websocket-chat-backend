package org.example.websocketchatbackend.exception;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String username) {
    super("User not found by username: " + username);
  }
}
