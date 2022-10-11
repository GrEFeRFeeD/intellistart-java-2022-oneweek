package com.intellias.intellistart.interviewplanning.model.period.validation;

import java.time.LocalTime;

public class LowerLimitValidator {
  public boolean isCorrect(LocalTime to){
    return to.getHour() >= 8;
  }
}
