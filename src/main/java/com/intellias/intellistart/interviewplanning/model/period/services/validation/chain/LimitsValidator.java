package com.intellias.intellistart.interviewplanning.model.period.services.validation.chain;

import java.time.LocalTime;

/**
 * Validator of period being between 8:00 and 22:00.
 */
public class LimitsValidator implements PeriodChainValidator {

  private static final short LOWER_LIMIT = 8;
  private static final short HIGHER_LIMIT = 22;

  public boolean isNotCorrect(LocalTime from, LocalTime to) {
    return isLowerNotCorrect(from) || isUpperNotCorrect(to);
  }

  private boolean isLowerNotCorrect(LocalTime from) {
    return from.getHour() < LOWER_LIMIT;
  }

  private boolean isUpperNotCorrect(LocalTime to) {
    int hour = to.getHour();

    if (hour > HIGHER_LIMIT) {
      return true;
    }
    if (hour < HIGHER_LIMIT) {
      return false;
    }
    //if hour == 22, minutes should be only 0
    return to.getMinute() > 0;
  }
}
