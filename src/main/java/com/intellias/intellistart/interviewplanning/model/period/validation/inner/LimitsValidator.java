package com.intellias.intellistart.interviewplanning.model.period.validation.inner;

import java.time.LocalTime;

public class LimitsValidator implements InnerValidator{
  public boolean isNotCorrect(LocalTime from, LocalTime to){
    return isLowerNotCorrect(from) || isUpperNotCorrect(to);
  }

  private boolean isLowerNotCorrect(LocalTime from){
    return from.getHour() < 8;
  }

  private boolean isUpperNotCorrect(LocalTime to){
    int hour = to.getHour();
    if (hour > 22){
      return true;
    }
    else if (hour < 22){
      return false;
    }
    else{
      return to.getMinute() > 0;
    }
  }
}
