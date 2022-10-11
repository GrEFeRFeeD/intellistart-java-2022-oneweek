package com.intellias.intellistart.interviewplanning.model.period.validation;

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
  void trueWhenCorrect1(){
    LocalTime time = LocalTime.of(19, 0);
    assertTrue(validator.isCorrect(time));
  }

  @Test
  void trueWhenCorrect2(){
    LocalTime time = LocalTime.of(19, 30);
    assertTrue(validator.isCorrect(time));
  }

  @Test
  void falseWhenIncorrect(){
    LocalTime time = LocalTime.of(19, 20);
    assertFalse(validator.isCorrect(time));
  }

  @Test
  void trueWhenIncorrectLogic(){
    LocalTime time = LocalTime.of(3, 30, 3);
    assertTrue(validator.isCorrect(time));
  }


}