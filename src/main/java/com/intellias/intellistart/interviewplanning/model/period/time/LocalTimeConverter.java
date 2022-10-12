package com.intellias.intellistart.interviewplanning.model.period.time;

import com.intellias.intellistart.interviewplanning.model.period.exceptions.InvalidBoundariesException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.springframework.stereotype.Component;

@Component
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
