package com.intellias.intellistart.interviewplanning.model.period._experimental_;

import com.intellias.intellistart.interviewplanning.model.period.exceptions.InvalidBoundariesException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.springframework.core.convert.converter.Converter;

@Deprecated
public class ExperimentTimeConverter implements Converter<String, LocalTime> {

  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

  @Override
  public LocalTime convert(String source) {
    try {
      return LocalTime.parse(source, formatter);
    } catch (DateTimeParseException e) {
      throw new InvalidBoundariesException();
    }
  }
}
