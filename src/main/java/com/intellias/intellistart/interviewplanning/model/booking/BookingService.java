package com.intellias.intellistart.interviewplanning.model.booking;

import com.intellias.intellistart.interviewplanning.controllers.dto.BookingDto;
import com.intellias.intellistart.interviewplanning.exceptions.BookingNotFoundException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotsAreNotIntersectingException;
import com.intellias.intellistart.interviewplanning.model.booking.validation.BookingValidator;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlotService;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService;
import com.intellias.intellistart.interviewplanning.model.period.PeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for Booking entity.
 */
@Service
public class BookingService {

  //TODO : fix javadoc
  private final BookingRepository bookingRepository;
  private final BookingValidator bookingValidator;
  private final PeriodService periodService;
  private final CandidateSlotService candidateSlotService;
  private final InterviewerSlotService interviewerSlotService;

  /**
   * Constructor.
   */
  @Autowired
  public BookingService(BookingRepository bookingRepository,
      BookingValidator bookingValidator, PeriodService periodService,
      CandidateSlotService candidateSlotService, InterviewerSlotService interviewerSlotService) {

    this.bookingRepository = bookingRepository;
    this.bookingValidator = bookingValidator;
    this.periodService = periodService;
    this.candidateSlotService = candidateSlotService;
    this.interviewerSlotService = interviewerSlotService;
  }

  /**
   * Find Booking by id from repository.
   *
   * @throws BookingNotFoundException if no booking with given id
   */
  public Booking findById(Long id) {
    return bookingRepository.findById(id).orElseThrow(BookingNotFoundException::new);
  }

  public Booking save(Booking booking){
    return bookingRepository.save(booking);
  }
}
