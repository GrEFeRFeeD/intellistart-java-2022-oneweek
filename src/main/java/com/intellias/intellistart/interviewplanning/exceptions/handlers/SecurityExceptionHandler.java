package com.intellias.intellistart.interviewplanning.exceptions.handlers;

import com.intellias.intellistart.interviewplanning.exceptions.old.ExceptionResponse;
import com.intellias.intellistart.interviewplanning.exceptions.SecurityException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Handler for security exceptions.
 * Handles authentication and authorization exceptions.
 */
@ControllerAdvice
public class SecurityExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * Method for handling SecurityException.
   */
  @ExceptionHandler(value = SecurityException.class)
  public ResponseEntity<Object> handleSecurityException(SecurityException exception,
      WebRequest webRequest) {

    var exceptionBody = new ExceptionResponse(exception.getName(), exception.getMessage());

    return handleExceptionInternal(exception, exceptionBody, new HttpHeaders(),
        exception.getResponseStatus(), webRequest);
  }
}
