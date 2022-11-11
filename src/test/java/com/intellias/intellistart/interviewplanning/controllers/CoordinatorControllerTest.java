package com.intellias.intellistart.interviewplanning.controllers;


import com.intellias.intellistart.interviewplanning.model.booking.BookingService;
import com.intellias.intellistart.interviewplanning.model.booking.validation.BookingValidator;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlotService;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService;
import com.intellias.intellistart.interviewplanning.model.period.PeriodService;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mockito;

class CoordinatorControllerTest {
  private static BookingService bookingService;
  private static BookingValidator bookingValidator;
  private static InterviewerSlotService interviewerSlotService;
  private static CandidateSlotService candidateSlotService;
  private static PeriodService periodService;

  private static CoordinatorController cut;

  @BeforeAll
  void initialize(){
    bookingService = Mockito.mock(BookingService.class);
    bookingValidator = Mockito.mock(BookingValidator.class);
    interviewerSlotService = Mockito.mock(InterviewerSlotService.class);
    candidateSlotService = Mockito.mock(CandidateSlotService.class);
    periodService = Mockito.mock(PeriodService.class);
    cut = new CoordinatorController(
        bookingService,
        bookingValidator,
        interviewerSlotService,
        candidateSlotService,
        periodService);
  }
}