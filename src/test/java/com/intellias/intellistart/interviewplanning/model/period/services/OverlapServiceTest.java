package com.intellias.intellistart.interviewplanning.model.period.services;

import static org.junit.jupiter.api.Assertions.*;

import com.intellias.intellistart.interviewplanning.model.period.Period;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class OverlapServiceTest {

  private static OverlapService overlapService;
  private static Period tenToTwoPeriod;
  @BeforeAll
  public static void initialize(){
    overlapService = new OverlapService();

    tenToTwoPeriod = new Period();
    tenToTwoPeriod.setFrom(LocalTime.of(10, 0));
    tenToTwoPeriod.setTo(LocalTime.of(14, 0));
  }

  @Test
  void overlapWhenCrossingSecondBefore(){

    Period period = new Period();
    period.setFrom(LocalTime.of(8, 30));
    period.setTo(LocalTime.of(11, 30));

    assertTrue(overlapService.isOverlap(tenToTwoPeriod, period));
  }


  @Test
  void overlapWhenCrossingFirstBefore(){

    Period period = new Period();
    period.setFrom(LocalTime.of(13, 30));
    period.setTo(LocalTime.of(18, 30));

    assertTrue(overlapService.isOverlap(tenToTwoPeriod, period));
  }

  @Test
  void overlapWhenInner(){

    Period period = new Period();
    period.setFrom(LocalTime.of(8, 0));
    period.setTo(LocalTime.of(16, 30));

    assertTrue(overlapService.isOverlap(tenToTwoPeriod, period));
  }

  @Test
  void noOverlapWhenBorders(){

    Period period = new Period();
    period.setFrom(LocalTime.of(14, 30));
    period.setTo(LocalTime.of(17, 30));

    assertFalse(overlapService.isOverlap(tenToTwoPeriod, period));
  }

  @Test
  void noOverlapWhenOkay(){

    Period period = new Period();
    period.setFrom(LocalTime.of(15, 30));
    period.setTo(LocalTime.of(17, 30));

    assertFalse(overlapService.isOverlap(tenToTwoPeriod, period));
  }
}