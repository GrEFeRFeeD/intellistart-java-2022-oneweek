package com.intellias.intellistart.interviewplanning.controllers;

import com.intellias.intellistart.interviewplanning.controllers.dto.BookingDto;
import com.intellias.intellistart.interviewplanning.exceptions.CandidateSlotNotFoundException;
import com.intellias.intellistart.interviewplanning.exceptions.InterviewerSlotNotFoundException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotsAreNotIntersectingException;
import com.intellias.intellistart.interviewplanning.model.booking.Booking;
import com.intellias.intellistart.interviewplanning.model.booking.BookingService;
import com.intellias.intellistart.interviewplanning.model.booking.validation.BookingValidator;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlotService;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService;
import com.intellias.intellistart.interviewplanning.model.period.PeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for processing requests from users with Coordinator role.
 */
@RestController
public class CoordinatorController {

  private final BookingService bookingService;
  private final BookingValidator bookingValidator;
  private final InterviewerSlotService interviewerSlotService;
  private final CandidateSlotService candidateSlotService;
  private final PeriodService periodService;

  /**
   * Constructor.
   */
  @Autowired
  public CoordinatorController(BookingService bookingService, BookingValidator bookingValidator,
      InterviewerSlotService interviewerSlotService, CandidateSlotService candidateSlotService,
      PeriodService periodService) {
    this.bookingService = bookingService;
    this.bookingValidator = bookingValidator;
    this.interviewerSlotService = interviewerSlotService;
    this.candidateSlotService = candidateSlotService;
    this.periodService = periodService;
  }

  /**
   * POST request method for updating booking by id.
   *
   * @param bookingDto request DTO
   *
   * @return ResponseEntity - Response of the saved updated object converted to a DTO.
   *
   * @throws InvalidBoundariesException if Period boundaries are Invalid
   * @throws CandidateSlotNotFoundException if CandidateSlot is not found
   * @throws InterviewerSlotNotFoundException if InterviewerSlot is not found
   * @throws SlotsAreNotIntersectingException if CandidateSlot, InterviewerSlot
   *     do not intersect with Period
   */
  @PostMapping("bookings/{id}")
  public ResponseEntity<BookingDto> updateBooking(
      @RequestBody BookingDto bookingDto,
      @PathVariable Long id) {

    Booking updatingBooking = bookingService.findById(id);
    Booking newDataBooking = getFromDto(bookingDto);

    bookingValidator.validateUpdating(updatingBooking, newDataBooking);
    populateFields(updatingBooking, newDataBooking);

    Booking savedBooking = bookingService.save(updatingBooking);
    return ResponseEntity.ok(new BookingDto(savedBooking));
  }

  /**
   * POST request method for adding booking.
   *
   * @param bookingDto request DTO
   *
   * @return ResponseEntity - Response of the saved created object converted to a DTO.
   *
   * @throws InvalidBoundariesException if Period boundaries are Invalid
   * @throws CandidateSlotNotFoundException if CandidateSlot is not found
   * @throws InterviewerSlotNotFoundException if InterviewerSlot is not found
   * @throws SlotsAreNotIntersectingException if CandidateSlot, InterviewerSlot
   *     do not intersect with Period
   */
  @PostMapping("bookings")
  public ResponseEntity<BookingDto> createBooking(
      @RequestBody BookingDto bookingDto) {

    Booking newBooking = getFromDto(bookingDto);

    bookingValidator.validateCreating(newBooking);
    Booking savedBooking = bookingService.save(newBooking);

    return ResponseEntity.ok(new BookingDto(savedBooking));
  }

  Booking getFromDto(BookingDto bookingDto) {
    Booking booking = new Booking();

    booking.setSubject(bookingDto.getSubject());
    booking.setDescription(bookingDto.getDescription());

    booking.setInterviewerSlot(interviewerSlotService
        .findById(bookingDto.getInterviewerSlotId()));

    booking.setCandidateSlot(candidateSlotService
        .findById(bookingDto.getCandidateSlotId()));

    booking.setPeriod(periodService.obtainPeriod(
        bookingDto.getFrom(),
        bookingDto.getTo()));

    System.out.println(booking);
    return booking;
  }

  private void populateFields(Booking booking, Booking newDataBooking) {
    booking.setSubject(newDataBooking.getSubject());
    booking.setDescription(newDataBooking.getDescription());
    booking.setInterviewerSlot(newDataBooking.getInterviewerSlot());
    booking.setCandidateSlot(newDataBooking.getCandidateSlot());
    booking.setPeriod(newDataBooking.getPeriod());
  }
}
