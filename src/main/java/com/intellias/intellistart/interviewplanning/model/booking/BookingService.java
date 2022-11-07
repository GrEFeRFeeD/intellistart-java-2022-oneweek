package com.intellias.intellistart.interviewplanning.model.booking;

import com.intellias.intellistart.interviewplanning.controllers.dto.BookingDto;
import com.intellias.intellistart.interviewplanning.exceptions.BookingNotFoundException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotsAreNotIntersectingException;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlot;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlotService;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.period.PeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for Booking entity.
 */
@Service
public class BookingService {

  private final BookingRepository bookingRepository;
  private final PeriodService periodService;
  private final CandidateSlotService candidateSlotService;
  private final InterviewerSlotService interviewerSlotService;

  private final BookingValidator bookingValidator;

  /**
   * Constructor.
   */
  @Autowired
  public BookingService(BookingRepository bookingRepository, PeriodService periodService,
      CandidateSlotService candidateSlotService, InterviewerSlotService interviewerSlotService,
      BookingValidator bookingValidator) {
    this.bookingRepository = bookingRepository;
    this.periodService = periodService;
    this.candidateSlotService = candidateSlotService;
    this.interviewerSlotService = interviewerSlotService;
    this.bookingValidator = bookingValidator;
  }

  /**
   * Find Booking by id from repository.
   *
   * @throws BookingNotFoundException if no booking with given id
   */
  public Booking findById(Long id) {
    return bookingRepository.findById(id).orElseThrow(BookingNotFoundException::new);
  }

  /**
   * Update booking: validate InterviewSlot, CandidateSlot, Period parameters and populate fields.
   *
   * @throws InvalidBoundariesException if validation failed
   * @throws SlotsAreNotIntersectingException if validation failed
   */
  public void updateBooking(Booking updatingBooking, BookingDto bookingDto) {
    InterviewerSlot interviewerSlot = interviewerSlotService
        .findById(bookingDto.getInterviewerSlotId());

    CandidateSlot candidateSlot = candidateSlotService
        .findById(bookingDto.getCandidateSlotId());

    Period period = periodService.obtainPeriod(
        bookingDto.getFrom(),
        bookingDto.getTo());

    bookingValidator.validateParameters(updatingBooking, interviewerSlot, candidateSlot, period);

    updatingBooking.setSubject(bookingDto.getSubject());
    updatingBooking.setDescription(bookingDto.getDescription());

    updatingBooking.setInterviewerSlot(interviewerSlot);
    updatingBooking.setCandidateSlot(candidateSlot);
    updatingBooking.setPeriod(period);
  }
}
