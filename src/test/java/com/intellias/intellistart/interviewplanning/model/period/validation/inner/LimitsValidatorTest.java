package com.intellias.intellistart.interviewplanning.model.period.validation.inner;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class LimitsValidatorTest {
  private static LimitsValidator validator;

  @BeforeAll
  public static void initialize(){
    validator = new LimitsValidator();
  }

  @Test
  void falseWhenOkay(){
    LocalTime from = LocalTime.of(19, 0);
    LocalTime to = LocalTime.of(8, 30);

    assertFalse(validator.isNotCorrect(from, to));
  }

  @Test
  void falseWhenBorderTime(){
    LocalTime from = LocalTime.of(8, 0);
    LocalTime to = LocalTime.of(22, 0);

    assertFalse(validator.isNotCorrect(from, to));
  }

  @Test
  void trueWhenUpperIncorrect(){
    LocalTime from = LocalTime.of(9, 0);
    LocalTime to = LocalTime.of(22, 30);

    assertTrue(validator.isNotCorrect(from, to));
  }

  @Test
  void trueWhenLowerIncorrect(){
    LocalTime from = LocalTime.of(3, 0);
    LocalTime to = LocalTime.of(21, 30);

    assertTrue(validator.isNotCorrect(from, to));
  }

  @Test
  void falseWhenAnotherLogicIncorrect(){
    LocalTime from = LocalTime.of(15, 44);
    LocalTime to = LocalTime.of(16, 7);

    assertFalse(validator.isNotCorrect(from, to));
  }
}