package com.intellias.intellistart.interviewplanning.model.period.services;

import static org.junit.jupiter.api.Assertions.*;

import com.intellias.intellistart.interviewplanning.exceptions.InvalidBoundariesException;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TimeConverterTest {

  private static TimeConverter cut;

  @BeforeAll
  public static void initialize(){
    cut = new TimeConverter();
  }

  @Test
  void convertWhenCorrectFormat() {
    LocalTime actual = cut.convert("19:00");
    LocalTime expected = LocalTime.of(19, 0);
    assertEquals(actual, expected);
  }

  @Test
  void notConvertWhenSecondsGiven() {
    assertThrows(InvalidBoundariesException.class, () ->
      cut.convert("19:00:33"));
  }

  @Test
  void notConvertWhenInvalidHourOrMinutes(){
    assertThrows(InvalidBoundariesException.class, () ->
      cut.convert("34:00"));
    assertThrows(InvalidBoundariesException.class, () ->
      cut.convert("11:77"));
  }

  @Test
  void convertWhenIncorrectLogic(){
    assertDoesNotThrow(() -> cut.convert("19:23"));
    assertDoesNotThrow(() -> cut.convert("23:00"));
  }
}