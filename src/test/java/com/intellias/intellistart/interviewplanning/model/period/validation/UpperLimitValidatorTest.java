package com.intellias.intellistart.interviewplanning.model.period.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class UpperLimitValidatorTest {
  private static UpperLimitValidator validator;

  @BeforeAll
  public static void initialize(){
    validator = new UpperLimitValidator();
  }

  @Test
  void trueWhenCorrect() {
    LocalTime time = LocalTime.of(19, 0);
    assertTrue(validator.isCorrect(time));
  }

  @Test
  void falseWhenNotIncorrect() {
    LocalTime time = LocalTime.of(23, 0);
    assertFalse(validator.isCorrect(time));
  }

  @Test
  void trueWhenIncorrectLogic() {
    LocalTime time = LocalTime.of(8, 17, 13);
    assertTrue(validator.isCorrect(time));
  }

}