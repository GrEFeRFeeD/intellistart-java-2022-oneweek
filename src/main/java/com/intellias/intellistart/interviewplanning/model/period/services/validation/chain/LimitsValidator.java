package com.intellias.intellistart.interviewplanning.model.period.services.validation.chain;

import java.time.LocalTime;

/**
 * Validator of period being between 8:00 and 22:00.
 */
public class LimitsValidator implements ChainValidator {

  public boolean isNotCorrect(LocalTime from, LocalTime to) {
    return isLowerNotCorrect(from) || isUpperNotCorrect(to);
  }

  private boolean isLowerNotCorrect(LocalTime from) {
    return from.getHour() < 8;
  }

  private boolean isUpperNotCorrect(LocalTime to) {
    int hour = to.getHour();
    if (hour > 22) {
      return true;
    }
    if (hour < 22) {
      return false;
    }
    return to.getMinute() > 0;
  }
}
