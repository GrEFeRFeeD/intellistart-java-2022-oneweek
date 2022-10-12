package com.intellias.intellistart.interviewplanning.model.period._experimental_;

import com.intellias.intellistart.interviewplanning.model.period.Period;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ExperimentPeriodValidator implements Validator{

  @Override
  public boolean supports(Class<?> clazz) {
    return Period.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {

  }
}
