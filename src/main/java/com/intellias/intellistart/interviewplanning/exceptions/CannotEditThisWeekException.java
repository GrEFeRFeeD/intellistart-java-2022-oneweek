package com.intellias.intellistart.interviewplanning.exceptions;

/**
 * Is thrown then we can not edit week that
 * we got in the DTO
 */
public class CannotEditThisWeekException extends RuntimeException {
  public CannotEditThisWeekException(){
    super("CannotEditThisWeekException");
  }
}
