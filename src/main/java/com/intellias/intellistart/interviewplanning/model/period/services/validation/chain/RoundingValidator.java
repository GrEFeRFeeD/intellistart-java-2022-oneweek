package com.intellias.intellistart.interviewplanning.model.period.services.validation.chain;

import java.time.LocalTime;
import lombok.NoArgsConstructor;

/**
 * Validator of period minutes being 30 or 0.
 */
@NoArgsConstructor
public class RoundingValidator implements PeriodChainValidator {

  @Override
  public boolean isNotCorrect(LocalTime from, LocalTime to) {
    return isNotCorrectSingle(from) || isNotCorrectSingle(to);
  }

  /**
   * Check if single LocalTime has incorrect rounding.
   */
  private boolean isNotCorrectSingle(LocalTime time) {
    int minutes = time.getMinute();
    return minutes != 30 && minutes != 0;
  }
}
