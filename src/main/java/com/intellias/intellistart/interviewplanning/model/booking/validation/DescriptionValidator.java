package com.intellias.intellistart.interviewplanning.model.booking.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DescriptionValidator implements
    ConstraintValidator<DescriptionConstraint, String> {

  private static final int MAX_DESCRIPTION_SIZE = 4;

  @Override
  public void initialize(DescriptionConstraint constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    return s.length() <= MAX_DESCRIPTION_SIZE;
  }
}
