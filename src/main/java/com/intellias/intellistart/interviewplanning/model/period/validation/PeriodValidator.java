package com.intellias.intellistart.interviewplanning.model.period.validation;

import com.intellias.intellistart.interviewplanning.model.period.exceptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.model.period.validation.inner.DurationValidator;
import com.intellias.intellistart.interviewplanning.model.period.validation.inner.InnerValidator;
import com.intellias.intellistart.interviewplanning.model.period.validation.inner.LimitsValidator;
import com.intellias.intellistart.interviewplanning.model.period.validation.inner.RoundingValidator;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PeriodValidator{

  private final List<InnerValidator> validators = new ArrayList<>(Arrays.asList(
      new LimitsValidator(),
      new RoundingValidator(),
      new DurationValidator()
  ));

  public void validate(LocalTime from, LocalTime to){
    for(InnerValidator validator: validators){
      if (validator.isNotCorrect(from, to) ){
        throw new InvalidBoundariesException();
      }
    }
  }

}
