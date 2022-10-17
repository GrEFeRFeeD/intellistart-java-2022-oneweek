package com.intellias.intellistart.interviewplanning.model.period.services.validation;

import static org.junit.jupiter.api.Assertions.*;

import com.intellias.intellistart.interviewplanning.model.period.exceptions.InvalidBoundariesException;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PeriodValidatorTest {

  private static PeriodValidator validator;

  @BeforeAll
  public static void initialize(){
    validator = new PeriodValidator();
  }


  @Test
  void correct(){
    LocalTime from = LocalTime.of(18, 0);
    LocalTime to = LocalTime.of(19, 30);

    assertDoesNotThrow(() -> validator.validate(from, to));
  }

  @Test
  void incorrectRounding(){
    LocalTime from = LocalTime.of(9, 1);
    LocalTime to = LocalTime.of(19, 30);

    assertThrows(InvalidBoundariesException.class, () ->
      validator.validate(from, to));
  }

  @Test
  void incorrectDuration(){
    LocalTime from = LocalTime.of(9, 0);
    LocalTime to = LocalTime.of(10, 0);

    assertThrows(InvalidBoundariesException.class, () ->
      validator.validate(from, to));
  }

  @Test
  void incorrectLimits(){
    LocalTime from = LocalTime.of(9, 30);
    LocalTime to = LocalTime.of(23, 0);

    assertThrows(InvalidBoundariesException.class, () ->
      validator.validate(from, to));
  }

  @Test
  void incorrectEverything(){
    LocalTime from = LocalTime.of(9, 31);
    LocalTime to = LocalTime.of(3, 0);

    assertThrows(InvalidBoundariesException.class, () ->
      validator.validate(from, to));
  }
}