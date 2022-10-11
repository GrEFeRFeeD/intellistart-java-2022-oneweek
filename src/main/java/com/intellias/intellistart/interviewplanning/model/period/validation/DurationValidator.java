package com.intellias.intellistart.interviewplanning.model.period.validation;

import java.time.Duration;
import java.time.LocalTime;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DurationValidator {

  private static final int MIN_DURATION = 90;

  public boolean isCorrect(LocalTime from, LocalTime to){
    Duration duration = Duration.between(from, to);

    int minutes = duration.toMinutesPart();
    int hours = duration.toHoursPart();

    return hours*60 + minutes >= MIN_DURATION;
  }

}
