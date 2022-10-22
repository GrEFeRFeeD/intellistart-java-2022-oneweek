package com.intellias.intellistart.interviewplanning.exceptions.handlers;

import com.intellias.intellistart.interviewplanning.exceptions.SecurityException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class SecurityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = SecurityException.class)
  public ResponseEntity<Object> handleSecurityException(SecurityException exception,
      WebRequest webRequest) {

    Map<String, String> exceptionBody = new LinkedHashMap<>();
    exceptionBody.put("errorCode", exception.getName());
    exceptionBody.put("errorMessage", exception.getMessage());

    return handleExceptionInternal(exception, exceptionBody, new HttpHeaders(),
        exception.getResponseStatus(), webRequest);
  }
}
