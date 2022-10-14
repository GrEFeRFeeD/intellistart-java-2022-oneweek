package com.intellias.intellistart.interviewplanning.model.period.services.validation.chain;

import java.time.LocalTime;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class RoundingValidator implements ChainValidator {

  public boolean isNotCorrect(LocalTime from, LocalTime to){
    return isNotCorrect(from) || isNotCorrect(to);
  }

  private boolean isNotCorrect(LocalTime time){
    int minutes = time.getMinute();
    return minutes != 30 && minutes != 0;
  }
}
