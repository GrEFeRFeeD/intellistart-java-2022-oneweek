package com.intellias.intellistart.interviewplanning.model.period.services.validation.inner;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DurationValidatorTest {
  private static DurationValidator validator;

  @BeforeAll
  public static void initialize(){
    validator = new DurationValidator();
  }

  @Test
  void falseWhenCorrect(){
    LocalTime from = LocalTime.of(8, 0);
    LocalTime to = LocalTime.of(10, 0);

    assertFalse(validator.isNotCorrect(from, to));
  }

  @Test
  void trueWhenIncorrectDifference(){
    LocalTime from = LocalTime.of(8, 0);
    LocalTime to = LocalTime.of(9, 0);

    assertTrue(validator.isNotCorrect(from, to));
  }

  @Test
  void falseWhenIncorrectLogic(){
    LocalTime from = LocalTime.of(3, 34, 8);
    LocalTime to = LocalTime.of(22, 33, 0);

    assertFalse(validator.isNotCorrect(from, to));
  }

  @Test
  void trueWhenNegativeDuration(){
    LocalTime from = LocalTime.of(18, 0);
    LocalTime to = LocalTime.of(15, 0);

    assertTrue(validator.isNotCorrect(from, to));
  }
}