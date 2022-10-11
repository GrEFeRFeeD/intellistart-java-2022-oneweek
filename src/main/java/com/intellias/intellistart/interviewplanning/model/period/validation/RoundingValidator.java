package com.intellias.intellistart.interviewplanning.model.period.validation;

import java.time.LocalTime;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RoundingValidator {

  public boolean isCorrect(LocalTime time){

    int minutes = time.getMinute();
    return minutes == 30 || minutes == 0;
  }
}
