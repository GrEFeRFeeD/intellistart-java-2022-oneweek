package com.intellias.intellistart.interviewplanning.model.period.validation;

import com.intellias.intellistart.interviewplanning.model.period.exceptions.InvalidBoundariesException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalTimeConverter{

  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");


  public LocalTime convert(String source) {
    try{
      return LocalTime.parse(source, formatter);
    }
    catch (DateTimeParseException e){
      throw new InvalidBoundariesException();
    }
  }


}
