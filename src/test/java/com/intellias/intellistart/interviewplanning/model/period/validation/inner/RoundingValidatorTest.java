package com.intellias.intellistart.interviewplanning.model.period.validation.inner;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class RoundingValidatorTest {

  private static RoundingValidator validator;

  @BeforeAll
  public static void initialize(){
    validator = new RoundingValidator();
  }

  @Test
  void falseWhenOkay(){
    LocalTime from = LocalTime.of(19, 0);
    LocalTime to = LocalTime.of(21, 30);

    assertFalse(validator.isNotCorrect(from, to));
  }

  @Test
  void trueWhenUpperIncorrect(){
    LocalTime from = LocalTime.of(19, 0);
    LocalTime to = LocalTime.of(21, 34);

    assertTrue(validator.isNotCorrect(from, to));
  }

  @Test
  void trueWhenLowerIncorrect(){
    LocalTime from = LocalTime.of(15, 17);
    LocalTime to = LocalTime.of(21, 0);

    assertTrue(validator.isNotCorrect(from, to));
  }

  @Test
  void falseWhenAnotherLogicIncorrect(){
    LocalTime from = LocalTime.of(15, 0);
    LocalTime to = LocalTime.of(16, 0);

    assertFalse(validator.isNotCorrect(from, to));
  }
}