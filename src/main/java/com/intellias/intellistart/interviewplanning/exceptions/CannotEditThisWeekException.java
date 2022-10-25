package com.intellias.intellistart.interviewplanning.exceptions;

/**
 * Thrown when we can not edit week that we got from the DTO.
 */
public class CannotEditThisWeekException extends RuntimeException {
  public CannotEditThisWeekException(){
    super("CannotEditThisWeekException");
  }
}
