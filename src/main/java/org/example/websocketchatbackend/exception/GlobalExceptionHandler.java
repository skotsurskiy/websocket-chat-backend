package org.example.websocketchatbackend.exception;

import static java.net.URI.create;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ProblemDetail.forStatusAndDetail;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
  private static final String VALIDATION_MESSAGE_DELIMITER = ", ";
  public static final String TITLE = "title";
  public static final String TIMESTAMP = "timestamp";
  public static final String STATUS = "status";
  public static final String DETAILS = "details";

  @ExceptionHandler(UserNameAlreadyExistsException.class)
  public ResponseEntity<Map<String, Object>> handleUserNameAlreadyExistsException(
      UserNameAlreadyExistsException ex
  ) {
    return getResponseEntity(BAD_REQUEST, "Username already exists", ex);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleUserNotFoundException(
      UserNotFoundException ex
  ) {
    return getResponseEntity(NOT_FOUND, "User not found", ex);
  }

  @ExceptionHandler(ChatAlreadyExistsException.class)
  public ResponseEntity<Map<String, Object>> handleChatAlreadyExistsException(
      ChatAlreadyExistsException ex
  ) {
    return getResponseEntity(BAD_REQUEST, "Chat already exists", ex);
  }

  @ExceptionHandler(ChatNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleChatNotFoundException(
      ChatNotFoundException ex
  ) {
    return getResponseEntity(NOT_FOUND, "Chat not found", ex);
  }

  @ExceptionHandler(MessageBlankContentException.class)
  public ResponseEntity<Map<String, Object>> handleMessageBlankContentException(
      MessageBlankContentException ex
  ) {
    return getResponseEntity(BAD_REQUEST, "Message is blank", ex);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, Object>> handleConstraintViolation(
      ConstraintViolationException ex
  ) {
    List<String> details = ex.getConstraintViolations().stream()
        .map(ConstraintViolation::getMessage)
        .toList();

    Map<String, Object> body = getDefaultBody(BAD_REQUEST, "Invalid enum type");
    body.put(DETAILS, details);

    return ResponseEntity.status(BAD_REQUEST).body(body);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {
    ProblemDetail problemDetail =
        forStatusAndDetail(
            BAD_REQUEST,
            ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(VALIDATION_MESSAGE_DELIMITER)));
    problemDetail.setType(create("validation-error"));
    problemDetail.setTitle("Field Validation Failed");
    return ResponseEntity.status(BAD_REQUEST).body(problemDetail);
  }

  private ResponseEntity<Map<String, Object>> getResponseEntity(
      HttpStatus status,
      String title,
      Exception ex
  ) {
    Map<String, Object> response = getDefaultBody(status, title);
    response.put(DETAILS, ex.getMessage());
    return ResponseEntity.status(status).body(response);
  }

  private Map<String, Object> getDefaultBody(
      HttpStatus status,
      String title
  ) {
    Map<String, Object> response = new LinkedHashMap<>();
    response.put(TITLE, title);
    response.put(TIMESTAMP, LocalDateTime.now());
    response.put(STATUS, status);

    return response;
  }
}
