package org.example.websocketchatbackend.exception;

public class ChatNotFoundException extends RuntimeException {
  public ChatNotFoundException(String details) {
    super(details);
  }
}
