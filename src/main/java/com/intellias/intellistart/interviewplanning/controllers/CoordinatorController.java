package com.intellias.intellistart.interviewplanning.controllers;

import com.intellias.intellistart.interviewplanning.controllers.dto.BookingDto;
import com.intellias.intellistart.interviewplanning.exceptions.CandidateSlotNotFoundException;
import com.intellias.intellistart.interviewplanning.exceptions.InterviewerSlotNotFoundException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotsAreNotIntersectingException;
import com.intellias.intellistart.interviewplanning.model.booking.Booking;
import com.intellias.intellistart.interviewplanning.model.booking.BookingService;
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

  @Autowired
  public CoordinatorController(BookingService bookingService) {
    this.bookingService = bookingService;
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
    bookingService.updateBooking(updatingBooking, bookingDto);

    return ResponseEntity.ok(new BookingDto(updatingBooking));
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

    Booking newBooking = new Booking();
    bookingService.updateBooking(newBooking, bookingDto);

    return ResponseEntity.ok(new BookingDto(newBooking));
  }
}
