package com.intellias.intellistart.interviewplanning.model.period.validation;

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
  void trueWhenCorrect(){
    LocalTime from = LocalTime.of(8, 0);
    LocalTime to = LocalTime.of(10, 0);
    assertTrue(validator.isCorrect(from, to));
  }

  @Test
  void falseWhenIncorrectDifference(){
    LocalTime from = LocalTime.of(8, 0);
    LocalTime to = LocalTime.of(9, 0);
    assertFalse(validator.isCorrect(from, to));
  }

  @Test
  void trueWhenIncorrectLogic(){
    LocalTime from = LocalTime.of(3, 34, 8);
    LocalTime to = LocalTime.of(22, 33, 0);
    assertTrue(validator.isCorrect(from, to));
  }

  @Test
  void falseWhenNegativeDuration(){
    LocalTime from = LocalTime.of(18, 0);
    LocalTime to = LocalTime.of(15, 0);
    assertFalse(validator.isCorrect(from, to));
  }
}