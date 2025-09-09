package org.example.websocketchatbackend.exception;

public class ChatAlreadyExistsException extends RuntimeException {
  public ChatAlreadyExistsException(String details) {
    super(details);
  }
}
