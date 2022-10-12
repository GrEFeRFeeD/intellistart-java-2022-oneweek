package com.intellias.intellistart.interviewplanning.model.period.validation.inner;

import java.time.LocalTime;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class RoundingValidator implements InnerValidator{

  public boolean isNotCorrect(LocalTime from, LocalTime to){
    return isNotCorrect(from) || isNotCorrect(to);
  }

  private boolean isNotCorrect(LocalTime time){
    int minutes = time.getMinute();
    return minutes != 30 && minutes != 0;
  }
}
