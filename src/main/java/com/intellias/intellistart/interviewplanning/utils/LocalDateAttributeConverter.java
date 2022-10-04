package com.intellias.intellistart.interviewplanning.utils;

import java.sql.Time;
import java.time.LocalTime;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converter of LocalTime to Time.
 * Persists LocalTime for JPA.
 */
@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalTime, Time> {

  @Override
  public Time convertToDatabaseColumn(LocalTime localTime) {
    return localTime == null ? null : Time.valueOf(localTime);
  }

  @Override
  public LocalTime convertToEntityAttribute(Time time) {
    return time == null ? null : time.toLocalTime();
  }
}
