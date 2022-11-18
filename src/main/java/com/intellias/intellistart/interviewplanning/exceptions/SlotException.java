package com.intellias.intellistart.interviewplanning.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Exception class for all slot logic exceptions.
 */
@Getter
public class SlotException extends Exception {

  /**
   * Enum that describes type of exception.
   */
  @AllArgsConstructor
  public enum SlotExceptionProfile {

    CANNOT_EDIT_THIS_WEEK("cannot_edit_this_week",
        "This week can't be edited.", HttpStatus.BAD_REQUEST),

    INVALID_BOUNDARIES("invalid_boundaries",
        "Time boundaries of slot or booking are invalid.", HttpStatus.BAD_REQUEST),

    INVALID_DAY_OF_WEEK("invalid_day_of_week",
        "Cannot arrange booking on this day", HttpStatus.BAD_REQUEST);

    private final String exceptionName;
    private final String exceptionMessage;
    private final HttpStatus responseStatus;
  }

  private final SlotException.SlotExceptionProfile slotExceptionProfile;

  public SlotException(SlotException.SlotExceptionProfile slotExceptionProfile) {
    super(slotExceptionProfile.exceptionMessage);
    this.slotExceptionProfile = slotExceptionProfile;
  }

  public String getName() {
    return slotExceptionProfile.exceptionName;
  }

  public HttpStatus getResponseStatus() {
    return slotExceptionProfile.responseStatus;
  }
}
