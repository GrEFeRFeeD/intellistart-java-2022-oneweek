package com.intellias.intellistart.interviewplanning.model.booking;

import com.intellias.intellistart.interviewplanning.controllers.dto.BookingDto;
import com.intellias.intellistart.interviewplanning.exceptions.BookingNotFoundException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotsAreNotIntersectingException;
import com.intellias.intellistart.interviewplanning.model.booking.validation.BookingData;
import com.intellias.intellistart.interviewplanning.model.booking.validation.BookingDataValidator;
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

  //TODO : fix javadoc
  private final BookingRepository bookingRepository;
  private final BookingDataValidator bookingDataValidator;
  private final PeriodService periodService;
  private final CandidateSlotService candidateSlotService;
  private final InterviewerSlotService interviewerSlotService;

  /**
   * Constructor.
   */
  @Autowired
  public BookingService(BookingRepository bookingRepository,
      BookingDataValidator bookingDataValidator, PeriodService periodService,
      CandidateSlotService candidateSlotService, InterviewerSlotService interviewerSlotService) {

    this.bookingRepository = bookingRepository;
    this.bookingDataValidator = bookingDataValidator;
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

  /**
   * Update booking: validate InterviewSlot, CandidateSlot, Period parameters and populate fields.
   *
   * @throws InvalidBoundariesException if validation failed
   * @throws SlotsAreNotIntersectingException if validation failed
   */
  public Booking getUpdatedBooking(Booking updatingBooking, BookingDto bookingDto) {
    BookingData bookingData = createBookingData(bookingDto);

    bookingDataValidator.validate(updatingBooking, bookingData);
    populateFields(updatingBooking, bookingData);

    return bookingRepository.save(updatingBooking);
  }

  BookingData createBookingData(BookingDto bookingDto){
    InterviewerSlot interviewerSlot = interviewerSlotService
        .findById(bookingDto.getInterviewerSlotId());

    CandidateSlot candidateSlot = candidateSlotService
        .findById(bookingDto.getCandidateSlotId());

    Period period = periodService.obtainPeriod(
        bookingDto.getFrom(),
        bookingDto.getTo());

    return new BookingData(
        bookingDto.getSubject(),
        bookingDto.getDescription(),
        interviewerSlot,
        candidateSlot,
        period);
  }

  //TODO : public private?
  void populateFields(Booking booking, BookingData bookingData){
    booking.setSubject(bookingData.getSubject());
    booking.setDescription(bookingData.getDescription());
    booking.setInterviewerSlot(bookingData.getInterviewerSlot());
    booking.setCandidateSlot(bookingData.getCandidateSlot());
    booking.setPeriod(bookingData.getPeriod());
  }
}
