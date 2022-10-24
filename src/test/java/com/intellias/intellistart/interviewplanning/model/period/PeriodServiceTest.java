package com.intellias.intellistart.interviewplanning.model.period;

import static org.junit.jupiter.api.Assertions.*;

import com.intellias.intellistart.interviewplanning.exceptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.model.period.services.OverlapService;
import com.intellias.intellistart.interviewplanning.model.period.services.TimeConverter;
import com.intellias.intellistart.interviewplanning.model.period.services.validation.PeriodValidator;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

class PeriodServiceTest {

  private static PeriodRepository repository;
  private static TimeConverter converter;
  private static PeriodValidator validator;
  private static OverlapService overlapService;
  private static PeriodService cut;

  private static List<Arguments> arguments;

  Period createPeriod(LocalTime from, LocalTime to){
    Period period = new Period();
    period.setFrom(from);
    period.setTo(to);

    return period;
  }

  @BeforeAll
  static void initialize(){
    repository = Mockito.mock(PeriodRepository.class);
    converter = Mockito.mock(TimeConverter.class);
    validator = Mockito.mock(PeriodValidator.class);
    overlapService = Mockito.mock(OverlapService.class);

    cut = new PeriodService(
        repository,
        converter,
        validator,
        overlapService
    );

    arguments = new ArrayList<>();
    arguments.add(Arguments.of(
        "19:30",
        "20:00",
        LocalTime.of(19, 30),
        LocalTime.of(20,0)));

    arguments.add(Arguments.of(
        "8:00",
        "16:00",
        LocalTime.of(8, 0),
        LocalTime.of(16,0)));

    arguments.add(Arguments.of(
        "12:30",
        "21:00",
        LocalTime.of(12, 30),
        LocalTime.of(21,0)));
  }

  @Test
  void exceptionWhenIncorrectConversion(){
    Mockito.when(converter.convert("19:00:33"))
        .thenThrow(InvalidBoundariesException.class);

    assertThrows(InvalidBoundariesException.class, () ->
        cut.getPeriod("19:00:33", "20:00"));
  }

  @Test
  void exceptionWhenIncorrectValidation(){
    String fromStr = "19:00";
    String toStr = "23:00";
    LocalTime from = LocalTime.of(19, 0);
    LocalTime to = LocalTime.of(23, 0);

    Mockito.when(converter.convert(fromStr)).thenReturn(from);
    Mockito.when(converter.convert(toStr)).thenReturn(to);

    Mockito.doThrow(InvalidBoundariesException.class).when(validator).validate(from, to);

    assertThrows(InvalidBoundariesException.class, () ->
        cut.getPeriod(fromStr, toStr));
  }

  static Stream<Arguments> provideArguments(){
    return Stream.of(
        Arguments.of(
            "19:30",
            "20:00",
            LocalTime.of(19, 30),
            LocalTime.of(20,0)),
        Arguments.of(
            "8:00",
            "16:00",
            LocalTime.of(8, 0),
            LocalTime.of(16,0)),
        Arguments.of(
            "12:30",
            "21:00",
            LocalTime.of(12, 30),
            LocalTime.of(21,0)));
  }

  @ParameterizedTest
  @MethodSource("provideArguments")
  void returnPeriodWhenPeriodNotExists(String fromStr, String toStr,
      LocalTime from, LocalTime to){

    Period expected = createPeriod(from, to);

    Mockito.when(converter.convert(fromStr)).thenReturn(from);
    Mockito.when(converter.convert(toStr)).thenReturn(to);

    Mockito.when(repository.findPeriodByFromAndTo(from, to)).thenReturn(Optional.empty());

    Period createdPeriod = createPeriod(from, to);
    Mockito.when(cut.createPeriod(from, to)).thenReturn(createdPeriod);

    Period actual = cut.getPeriod(fromStr, toStr);

    assertEquals(actual, expected);
  }

  @ParameterizedTest
  @MethodSource("provideArguments")
  void returnPeriodWhenPeriodExists(String fromStr, String toStr,
  LocalTime from, LocalTime to){

    Period expected = createPeriod(from, to);

    Mockito.when(converter.convert(fromStr)).thenReturn(from);
    Mockito.when(converter.convert(toStr)).thenReturn(to);

    Period foundPeriod = createPeriod(from, to);
    Mockito.when(repository.findPeriodByFromAndTo(from, to)).thenReturn(Optional.of(foundPeriod));

    Period actual = cut.getPeriod(fromStr, toStr);

    assertEquals(actual, expected);
  }
}