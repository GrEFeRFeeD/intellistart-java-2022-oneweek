package com.intellias.intellistart.interviewplanning.model.period.services.validation;

import com.intellias.intellistart.interviewplanning.exceptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.model.period.services.validation.chain.DurationValidator;
import com.intellias.intellistart.interviewplanning.model.period.services.validation.chain.LimitsValidator;
import com.intellias.intellistart.interviewplanning.model.period.services.validation.chain.PeriodChainValidator;
import com.intellias.intellistart.interviewplanning.model.period.services.validation.chain.RoundingValidator;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Business logic validator class.
 */
@Component
public class PeriodValidator {

  private final List<PeriodChainValidator> validators = new ArrayList<>(Arrays.asList(
      new LimitsValidator(),
      new RoundingValidator(),
      new DurationValidator()
  ));

  /**
   * Proceed all needed business validations before creating Period object.
   *
   * @param from - lower time boundary
   * @param to - upper time boundary
   */
  public void validate(LocalTime from, LocalTime to) {
    for (PeriodChainValidator validator : validators) {
      if (validator.isNotCorrect(from, to)) {
        throw new InvalidBoundariesException();
      }
    }
  }

}
