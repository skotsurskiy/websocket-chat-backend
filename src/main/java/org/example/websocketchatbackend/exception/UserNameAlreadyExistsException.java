package org.example.websocketchatbackend.exception;

public class UserNameAlreadyExistsException extends RuntimeException {
  public UserNameAlreadyExistsException(String username) {
    super("User with username %s already exists".formatted(username));
  }
}
