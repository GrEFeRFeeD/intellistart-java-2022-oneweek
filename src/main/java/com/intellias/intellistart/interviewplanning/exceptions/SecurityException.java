package com.intellias.intellistart.interviewplanning.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SecurityException extends RuntimeException{

  @AllArgsConstructor
  public enum SecurityExceptionProfile {

    UNAUTHENTICATED("not_authenticated_exception",
        "You are not authenticated to perform this action.", HttpStatus.UNAUTHORIZED),

    BAD_TOKEN("bad_token_exception",
        "Token does not start with \"Bearer\".", HttpStatus.UNAUTHORIZED),

    EXPIRED_TOKEN("expired_token_exception",
        "Token has expired.", HttpStatus.UNAUTHORIZED),

    BAD_CREDENTIALS("bad_credentials",
        "Incorrect credentials.", HttpStatus.UNAUTHORIZED);

    private final String exceptionName;
    private final String exceptionMessage;
    private final HttpStatus responseStatus;

  }

  private final SecurityExceptionProfile securityExceptionProfile;

  public SecurityException(SecurityExceptionProfile exceptionProfile) {
    super(exceptionProfile.exceptionMessage);
    this.securityExceptionProfile = exceptionProfile;
  }

  public String getName() {
    return securityExceptionProfile.exceptionName;
  }

  public HttpStatus getResponseStatus() {
    return securityExceptionProfile.responseStatus;
  }
}
