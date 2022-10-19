package com.intellias.intellistart.interviewplanning.model.period;

import static org.junit.jupiter.api.Assertions.*;

import com.intellias.intellistart.interviewplanning.exceptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.model.period.services.OverlapService;
import com.intellias.intellistart.interviewplanning.model.period.services.TimeConverter;
import com.intellias.intellistart.interviewplanning.model.period.services.validation.PeriodValidator;

import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

class PeriodServiceTest {

  private static PeriodRepository periodRepository;

  private static PeriodService periodService;

  private static TimeConverter converter;

  private Period createPeriod(LocalTime from, LocalTime to){
    Period period = new Period();
    period.setFrom(from);
    period.setTo(to);

    return period;
  }

  @BeforeAll
  static void initialize(){
       periodRepository = Mockito.mock(PeriodRepository.class);
       periodService = new PeriodService(
           periodRepository, new TimeConverter(),
           new PeriodValidator(),
           new OverlapService()
       );

       converter = new TimeConverter();
  }

  @Test
  void exceptionWhenIncorrectTimeFormat(){
    assertThrows(InvalidBoundariesException.class, () ->
        periodService.getPeriod("19:00:33", "20:00"));
  }

  @Test
  void exceptionWhenIncorrectBoundaries(){
    assertThrows(InvalidBoundariesException.class, () ->
        periodService.getPeriod("19:00", "23:00"));
  }

  @Test
  void exceptionWhenIncorrectDuration(){
    assertThrows(InvalidBoundariesException.class, () ->
        periodService.getPeriod("19:00", "20:00"));
  }

  @Test
  void exceptionWhenIncorrectRounding(){
    assertThrows(InvalidBoundariesException.class, () ->
        periodService.getPeriod("18:00", "20:01"));
  }

  @Test
  void giveCorrectWhenNotExistsInDatabase(){
    String fromStr = "18:00";
    String toStr = "20:30";
    LocalTime from = converter.convert(fromStr);
    LocalTime to = converter.convert(toStr);

    Period expected = createPeriod(from, to);

    Mockito.when(periodRepository.findPeriod(from, to)).thenReturn(Optional.empty());
    Mockito.when(periodRepository.save(expected)).thenReturn(expected);

    Period resulting = periodService.getPeriod(fromStr, toStr);

    assertEquals(expected, resulting);
  }

  @Test
  void giveCorrectWhenExistsInDatabase(){
    String fromStr = "19:00";
    String toStr = "21:30";
    LocalTime from = converter.convert(fromStr);
    LocalTime to = converter.convert(toStr);

    Period expected = createPeriod(from, to);

    Mockito.when(periodRepository.findPeriod(from, to)).thenReturn(Optional.of(expected));
    Mockito.when(periodRepository.save(expected)).thenReturn(expected);

    Period resulting = periodService.getPeriod(fromStr, toStr);

    assertEquals(expected, resulting);
  }
}