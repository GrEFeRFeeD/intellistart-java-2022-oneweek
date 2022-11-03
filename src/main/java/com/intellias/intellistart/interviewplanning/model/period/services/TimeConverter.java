package com.intellias.intellistart.interviewplanning.model.period.services;

import com.intellias.intellistart.interviewplanning.exceptions.InvalidBoundariesException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.springframework.stereotype.Component;

/**
 * Utils class to perform time operations.
 */

@Component
public class TimeConverter {

  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

  /**
   * Conversion method.
   */
  public LocalTime convert(String source) {
    try {
      return LocalTime.parse(source, formatter);
    } catch (DateTimeParseException e) {
      throw new InvalidBoundariesException();
    }
  }

  public int getDurationMinutes(LocalTime lower, LocalTime upper){
    Duration duration = Duration.between(lower, upper);

    int minutes = duration.toMinutesPart();
    int hours = duration.toHoursPart();

    return hours * 60 + minutes;
  }
}
