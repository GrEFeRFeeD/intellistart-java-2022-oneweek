package com.intellias.intellistart.interviewplanning.model.period.services.validation.chain;

import java.time.Duration;
import java.time.LocalTime;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DurationValidator implements ChainValidator {

  private static final int MIN_DURATION = 90;


  public boolean isNotCorrect(LocalTime from, LocalTime to){
    Duration duration = Duration.between(from, to);

    int minutes = duration.toMinutesPart();
    int hours = duration.toHoursPart();

    return hours*60 + minutes < MIN_DURATION;
  }

}
