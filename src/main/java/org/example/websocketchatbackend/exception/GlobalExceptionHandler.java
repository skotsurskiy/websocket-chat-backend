package org.example.websocketchatbackend.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  public static final String TITLE_COLON_SEPARATOR = ": ";
  public static final String TITLE = "title";
  public static final String TIMESTAMP = "timestamp";
  public static final String STATUS = "status";
  public static final String DETAILS = "details";

  @ExceptionHandler(UserNameAlreadyExistsException.class)
  public ResponseEntity<Map<String, Object>> handleUserNameAlreadyExistsException(
      UserNameAlreadyExistsException ex
  ) {
    return getResponseEntity(HttpStatus.BAD_REQUEST, "Username already exists", ex);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleUserNotFoundException(
      UserNotFoundException ex
  ) {
    return getResponseEntity(HttpStatus.NOT_FOUND, "User not found", ex);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationException(
      MethodArgumentNotValidException ex
  ) {
    List<String> details = ex.getBindingResult().getAllErrors().stream()
        .map(this::getErrorMessage)
        .toList();

    Map<String, Object> body =
        getDefaultBody(HttpStatus.BAD_REQUEST, "Request validation error");
    body.put(DETAILS, details);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, Object>> handleConstraintViolation(
      ConstraintViolationException ex
  ) {
    List<String> details = ex.getConstraintViolations().stream()
        .map(ConstraintViolation::getMessage)
        .toList();

    Map<String, Object> body = getDefaultBody(HttpStatus.BAD_REQUEST, "Invalid enum type");
    body.put(DETAILS, details);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
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

  private String getErrorMessage(ObjectError objectError) {
    if (objectError instanceof FieldError fieldError) {
      String defaultMessage = fieldError.getDefaultMessage();
      String field = fieldError.getField();
      return field + TITLE_COLON_SEPARATOR + defaultMessage;
    }
    return objectError.getDefaultMessage();
  }
}
