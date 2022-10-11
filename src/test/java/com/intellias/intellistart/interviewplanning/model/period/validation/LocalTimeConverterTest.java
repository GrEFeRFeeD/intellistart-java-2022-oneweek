package com.intellias.intellistart.interviewplanning.model.period.validation;

import static org.junit.jupiter.api.Assertions.*;

import com.intellias.intellistart.interviewplanning.model.period.exceptions.InvalidBoundariesException;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class LocalTimeConverterTest {

  private static LocalTimeConverter converter;

  @BeforeAll
  public static void initialize(){
    converter = new LocalTimeConverter();
  }

  @Test
  void convertWhenCorrectFormat() {
    LocalTime got = converter.convert("19:00");
    LocalTime expected = LocalTime.of(19, 0);
    assertEquals(got, expected);
  }

  @Test
  void notConvertWhenSecondsGiven() {
    assertThrows(InvalidBoundariesException.class, () -> {
      converter.convert("19:00:33");
    });
  }

  @Test
  void notConvertWhenInvalidHourOrMinutes(){
    assertThrows(InvalidBoundariesException.class, () -> {
      converter.convert("34:00");
    });
    assertThrows(InvalidBoundariesException.class, () -> {
      converter.convert("11:77");
    });
  }

  @Test
  void convertWhenIncorrectLogic(){
    converter.convert("19:23");
    converter.convert("23:00");
  }
}