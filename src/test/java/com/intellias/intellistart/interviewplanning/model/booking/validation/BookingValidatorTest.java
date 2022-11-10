package com.intellias.intellistart.interviewplanning.model.booking.validation;

import static org.junit.jupiter.api.Assertions.*;

import com.intellias.intellistart.interviewplanning.exceptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.model.booking.Booking;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlot;
import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.period.PeriodService;
import com.intellias.intellistart.interviewplanning.model.period.services.TimeService;
import com.intellias.intellistart.interviewplanning.model.week.Week;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

class BookingValidatorTest {
  private static PeriodService periodService;
  private static TimeService timeService;
  private static BookingValidator cut;

  private static Booking invalidBooking1;

  private static Booking invalidBooking2;

  private static Period wrongPeriod;

  private static Week week1;
  private static LocalDate week1TuesdayDate;
  private static LocalDate week1WednesdayDate;
  private static InterviewerSlot interviewerSlot1;
  private static InterviewerSlot interviewerSlot2;
  private static InterviewerSlot interviewerSlot3;
  private static CandidateSlot candidateSlot1;
  private static CandidateSlot candidateSlot2;
  private static CandidateSlot candidateSlot3;
  private static Period interviewerSlotPeriod1;
  private static Period candidateSlotPeriod1;
  private static Period slotPeriod3;
  private static Period bookingPeriod1;
  private static Period bookingPeriod2;
  private static Period bookingPeriod3;

  private static Booking booking1;
  private static Booking booking2;
  private static Booking bookingCreated1;
  private static Booking booking4;
  private static Booking booking5;
  private static Booking booking6;


  @BeforeEach
  void initialize(){
    periodService = Mockito.mock(PeriodService.class);
    timeService = Mockito.mock(TimeService.class);
    cut = new BookingValidator(periodService, timeService);

    wrongPeriod = new Period();
    wrongPeriod.setFrom(LocalTime.of(10, 0));
    wrongPeriod.setTo(LocalTime.of(12, 0));

    week1 = new Week(43L, null);
    week1TuesdayDate = LocalDate.of(2022, 1, 1);
    week1WednesdayDate = LocalDate.of(2022, 1, 2);

    interviewerSlotPeriod1 = new Period();
    interviewerSlotPeriod1.setFrom(LocalTime.of(8, 0));
    interviewerSlotPeriod1.setTo(LocalTime.of(17, 30));

    candidateSlotPeriod1 = new Period();
    candidateSlotPeriod1.setFrom(LocalTime.of(9, 30));
    candidateSlotPeriod1.setTo(LocalTime.of(20, 0));

    slotPeriod3 = new Period();
    slotPeriod3.setFrom(LocalTime.of(18, 0));
    slotPeriod3.setTo(LocalTime.of(21, 30));

    bookingPeriod1 = new Period();
    bookingPeriod1.setFrom(LocalTime.of(10, 30));
    bookingPeriod1.setTo(LocalTime.of(12, 0));

    bookingPeriod2 = new Period();
    bookingPeriod2.setFrom(LocalTime.of(9, 0));
    bookingPeriod2.setTo(LocalTime.of(10, 30));

    bookingPeriod3 = new Period();
    bookingPeriod3.setFrom(LocalTime.of(13, 0));
    bookingPeriod3.setTo(LocalTime.of(14, 30));

    interviewerSlot1 = new InterviewerSlot(
        1L, week1, DayOfWeek.TUE, interviewerSlotPeriod1,
        new HashSet<>(Arrays.asList(booking1, booking2)), null);

    candidateSlot1 = new CandidateSlot(
        1L, week1TuesdayDate, candidateSlotPeriod1,
        new HashSet<>(Arrays.asList(booking1)), null);

    candidateSlot2 = new CandidateSlot(
        2L, week1TuesdayDate, candidateSlotPeriod1,
        new HashSet<>(Arrays.asList(booking2)), null);

    candidateSlot3 = new CandidateSlot(
        3L, week1TuesdayDate, candidateSlotPeriod1,
        new HashSet<>(), null);

    booking1 = new Booking(
        1L, "interview", "Maks Kostyshen",
        interviewerSlot1, candidateSlot1, bookingPeriod1);

    booking2 = new Booking(
        2L, "interview", "Daria Pavliuk",
        interviewerSlot1, candidateSlot2, bookingPeriod2);

    bookingCreated1 = new Booking(
        3L, "interview", "Anisimov1",
        interviewerSlot1, candidateSlot3, bookingPeriod3);
  }

  @ParameterizedTest
  @MethodSource("provideCorrectArguments")
  void failWhenInvalidPeriodDuration(Booking booking){
    Mockito.when(timeService.calculateDurationMinutes(
        booking.getPeriod().getFrom(), booking.getPeriod().getTo())).thenReturn(120);

    assertThrows(InvalidBoundariesException.class, () -> cut.validateCreating(booking));
  }

  static Stream<Arguments> provideInvalidPeriodDurationArguments(){
    return Stream.of(Arguments.arguments(
        bookingCreated1));
  }

  @ParameterizedTest
  @MethodSource("provideInvalidPeriodDurationArguments")
  void notFailWhenCorrect(Booking booking){

    Mockito.when(timeService.calculateDurationMinutes(
        booking.getPeriod().getFrom(), booking.getPeriod().getTo())).thenReturn(90);
    assertDoesNotThrow(() -> cut.validateCreating(booking));
  }

  static Stream<Arguments> provideNotIntersectingSlotsArguments(){
    return Stream.of(Arguments.arguments(
        invalidBooking1,invalidBooking2));
  }

  @ParameterizedTest
  @MethodSource("provideNotIntersectingSlotsArguments")
  void failWhenSlotsDoNotIntersect(){

  }

  static Stream<Arguments> provideInvalidSubjectArguments(){
    return Stream.of(Arguments.arguments(
        new Booking(null, "subject", "desc",
            null, null, wrongPeriod),
        new Booking(null, "invalid_subject", "desc",
            null, null, wrongPeriod)));
  }

  @ParameterizedTest
  @MethodSource("provideInvalidSubjectArguments")
  void failWhenInvalidSubject(){

  }

  static Stream<Arguments> provideOverlappingWithOtherBookingPeriodsArguments(){
    return Stream.of(Arguments.arguments(
        invalidBooking1,invalidBooking2));
  }

  @ParameterizedTest
  @MethodSource("provideOverlappingWithOtherBookingPeriodsArguments")
  void failWhenBookingPeriodOverlappingWithOtherBookingPeriods(){

  }
}