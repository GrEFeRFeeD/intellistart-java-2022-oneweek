package com.intellias.intellistart.interviewplanning.model.period.validation;

import java.time.LocalTime;

public class UpperLimitValidator {

  public boolean isCorrect(LocalTime to){
    return to.getHour() <= 21;
  }
}
