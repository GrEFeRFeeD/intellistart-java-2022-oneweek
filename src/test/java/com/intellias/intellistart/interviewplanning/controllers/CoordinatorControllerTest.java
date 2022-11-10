package com.intellias.intellistart.interviewplanning.controllers;

import static org.junit.jupiter.api.Assertions.*;

class CoordinatorControllerTest {
//  private static BookingRepository bookingRepository;
//  private static BookingValidator bookingValidator;
//  private static PeriodService periodService;
//  private static CandidateSlotService candidateSlotService;
//  private static InterviewerSlotService interviewerSlotService;
//  private static BookingService cut;
//
//  @BeforeAll
//  static void initialize() {
//    bookingRepository = Mockito.mock(BookingRepository.class);
//    bookingValidator = Mockito.mock(BookingValidator.class);
//    periodService = Mockito.mock(PeriodService.class);
//    interviewerSlotService = Mockito.mock(InterviewerSlotService.class);
//    candidateSlotService = Mockito.mock(CandidateSlotService.class);
//
//    cut = new BookingService(
//        bookingRepository,
//        bookingValidator,
//        periodService,
//        candidateSlotService,
//        interviewerSlotService
//    );
//  }
//  static Stream<Arguments> provideNotExistingInterviewerSlotArguments(){
//    return Stream.of(Arguments.of(
//        new BookingDto(567887654L, 1L,
//            "14:00", "15:30",
//            "interview", "Kostyshen Maksym"),
//        new BookingDto(678L, 2L,
//            "14:00", "15:30",
//            "planning", "Sirenko Sasha"))
//    );
//  }
//  @ParameterizedTest
//  @MethodSource("provideNotExistingInterviewerSlotArguments")
//  void whenInterviewerSlotNotExistsFailCreateBookingData(BookingDto bookingDto){
//    Mockito.when(interviewerSlotService
//        .findById(bookingDto.getInterviewerSlotId()))
//        .thenThrow(InterviewerSlotNotFoundException.class);
//
//    assertThrows(InterviewerSlotNotFoundException.class,
//        () -> cut.getFromDto(bookingDto));
//  }
//
//  static Stream<Arguments> provideNotExistingCandidateSlotArguments(){
//    return Stream.of(Arguments.of(
//        new BookingDto(1L, 123434L,
//            "14:00", "15:30",
//            "interview", "Kostyshen Maksym"),
//        new BookingDto(6L, 22332L,
//            "14:00", "15:30",
//            "planning", "Sirenko Sasha"))
//    );
//  }
//  @ParameterizedTest
//  @MethodSource("provideNotExistingCandidateSlotArguments")
//  void whenCandidateSlotNotExistsFailCreateBookingData(BookingDto bookingDto){
//    Mockito.when(candidateSlotService
//            .findById(bookingDto.getCandidateSlotId()))
//            .thenThrow(CandidateSlotNotFoundException.class);
//
//    assertThrows(CandidateSlotNotFoundException.class,
//        () -> cut.getFromDto(bookingDto));
//  }
//
//  static Stream<Arguments> provideInvalidPeriodArguments(){
//    return Stream.of(Arguments.of(
//        new BookingDto(1L, 123434L,
//            "14:00", "15:30",
//            "interview", "Kostyshen Maksym"),
//        new BookingDto(6L, 22332L,
//            "14:00", "15:30",
//            "planning", "Sirenko Sasha"))
//    );
//  }
//  @ParameterizedTest
//  @MethodSource("provideInvalidPeriodArguments")
//  void whenInvalidPeriodFailCreateBookingData(BookingDto bookingDto){
//    Mockito.when(periodService
//        .obtainPeriod(bookingDto.getFrom(), bookingDto.getTo()))
//        .thenThrow(InvalidBoundariesException.class);
//
//    assertThrows(InvalidBoundariesException.class,
//        ()-> cut.getFromDto(bookingDto));
//  }
//
//  static Stream<Arguments> provideProperArguments(){
//    return Stream.of(
//        Arguments.of(
//        new BookingDto(1L, 123434L,
//            "14:00", "15:30",
//            "interview", "Kostyshen Maksym"),
//        new BookingData("interview", "Kostyshen Maksym",
//            new InterviewerSlot(), new CandidateSlot(), new Period())),
//        Arguments.of(
//        new BookingDto(6L, 22332L,
//            "14:00", "15:30",
//            "planning", "Sirenko Sasha"),
//        new BookingData("planning", "Sirenko Sasha",
//            new InterviewerSlot(), new CandidateSlot(), new Period()))
//    );
//  }
//  @ParameterizedTest
//  @MethodSource("provideProperArguments")
//  void whenOkayCreateBookingDataCorrectly(BookingDto bookingDto, BookingData expected){
//    Mockito.when(interviewerSlotService
//        .findById(bookingDto.getInterviewerSlotId()))
//        .thenReturn(expected.getInterviewerSlot());
//
//    Mockito.when(candidateSlotService
//            .findById(bookingDto.getInterviewerSlotId()))
//        .thenReturn(expected.getCandidateSlot());
//
//    Mockito.when(periodService
//        .obtainPeriod(bookingDto.getFrom(), bookingDto.getTo()))
//        .thenReturn(expected.getPeriod());
//
//    BookingData actual = cut.getFromDto(bookingDto);
//    assertEquals(expected, actual);
//  }
}