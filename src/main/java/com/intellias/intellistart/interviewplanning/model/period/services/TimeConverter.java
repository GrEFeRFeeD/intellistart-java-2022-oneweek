package com.intellias.intellistart.interviewplanning.model.period.services;

import com.intellias.intellistart.interviewplanning.model.period.exceptions.InvalidBoundariesException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.springframework.stereotype.Component;

/**
 * Converts String to LocalTime.
 */
@Component
public class TimeConverter {

  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

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


}
