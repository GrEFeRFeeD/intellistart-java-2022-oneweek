package com.intellias.intellistart.interviewplanning.model.period.services;

import static org.junit.jupiter.api.Assertions.*;

import com.intellias.intellistart.interviewplanning.model.period.Period;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class OverlapServiceTest {

  private static OverlapService overlapService;

  @BeforeAll
  public static void initialize(){
    overlapService = new OverlapService();
  }

  @Test
  void overlapWhenCrossingSecondBefore(){
    Period p1 = new Period();
    p1.setFrom(LocalTime.of(13, 0));
    p1.setTo(LocalTime.of(16, 30));

    Period p2 = new Period();
    p2.setFrom(LocalTime.of(12, 30));
    p2.setTo(LocalTime.of(15, 30));

    assertTrue(overlapService.isOverlap(p1, p2));
  }


  @Test
  void overlapWhenCrossingFirstBefore(){
    Period p1 = new Period();
    p1.setFrom(LocalTime.of(13, 0));
    p1.setTo(LocalTime.of(17, 30));

    Period p2 = new Period();
    p2.setFrom(LocalTime.of(16, 30));
    p2.setTo(LocalTime.of(18, 30));

    assertTrue(overlapService.isOverlap(p1, p2));
  }

  @Test
  void overlapWhenInner(){
    Period p1 = new Period();
    p1.setFrom(LocalTime.of(13, 0));
    p1.setTo(LocalTime.of(18, 30));

    Period p2 = new Period();
    p2.setFrom(LocalTime.of(14, 0));
    p2.setTo(LocalTime.of(17, 30));

    assertTrue(overlapService.isOverlap(p1, p2));
  }

  @Test
  void noOverlapWhenBorders(){
    Period p1 = new Period();
    p1.setFrom(LocalTime.of(13, 0));
    p1.setTo(LocalTime.of(15, 30));

    Period p2 = new Period();
    p2.setFrom(LocalTime.of(15, 30));
    p2.setTo(LocalTime.of(17, 30));

    assertFalse(overlapService.isOverlap(p1, p2));
  }

  @Test
  void noOverlapWhenOkay(){
    Period p1 = new Period();
    p1.setFrom(LocalTime.of(10, 0));
    p1.setTo(LocalTime.of(12, 0));

    Period p2 = new Period();
    p2.setFrom(LocalTime.of(15, 30));
    p2.setTo(LocalTime.of(17, 30));

    assertFalse(overlapService.isOverlap(p1, p2));
  }
}