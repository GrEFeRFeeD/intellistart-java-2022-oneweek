package com.intellias.intellistart.interviewplanning.exceptions;

/**
 * Is thrown then can't obtain Period with given boundaries:
 *    can't convert to LocalTime,
 *    invalid business logic.
 */
public class InvalidBoundariesException extends IllegalArgumentException {

  public InvalidBoundariesException(){
    super();
  }
}
