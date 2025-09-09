package org.example.websocketchatbackend.exception;

public class MessageBlankContentException extends RuntimeException {
  public MessageBlankContentException() {
    super("Message can't be blank");
  }
}
