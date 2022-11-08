package com.intellias.intellistart.interviewplanning.model.booking;

import static org.junit.jupiter.api.Assertions.*;

import com.intellias.intellistart.interviewplanning.model.booking.validation.BookingData;
import com.intellias.intellistart.interviewplanning.model.booking.validation.BookingDataValidator;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlotService;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService;
import com.intellias.intellistart.interviewplanning.model.period.PeriodRepository;
import com.intellias.intellistart.interviewplanning.model.period.PeriodService;
import com.intellias.intellistart.interviewplanning.model.period.services.TimeService;
import com.intellias.intellistart.interviewplanning.model.period.services.validation.PeriodValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class BookingServiceTest {

  private static BookingRepository bookingRepository;
  private static BookingDataValidator bookingDataValidator;
  private static PeriodService periodService;
  private static CandidateSlotService candidateSlotService;
  private static InterviewerSlotService interviewerSlotService;
  private static BookingService cut;

  @BeforeAll
  static void initialize() {
    bookingRepository = Mockito.mock(BookingRepository.class);
    bookingDataValidator = Mockito.mock(BookingDataValidator.class);
    periodService = Mockito.mock(PeriodService.class);
    interviewerSlotService = Mockito.mock(InterviewerSlotService.class);
    candidateSlotService = Mockito.mock(CandidateSlotService.class);

    cut = new BookingService(
        bookingRepository,
        bookingDataValidator,
        periodService,
        candidateSlotService,
        interviewerSlotService
    );
  }

  @Test
  void whenInterviewerSlotNotFoundFailCreateBookingData(){
  }

  @Test
  void whenCandidateSlotNotFoundFailCreateBookingData(){
  }

  @Test
  void whenInvalidPeriodFailCreateBookingData(){
  }

  @Test
  void whenWhenOkayCreateBookingDataCorrectly(){
  }
}