package com.intellias.intellistart.interviewplanning.controllers;

import com.intellias.intellistart.interviewplanning.controllers.dto.BookingDto;
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

  @PostMapping("bookings/{id}")
  public ResponseEntity<BookingDto> updateBooking(
      @RequestBody BookingDto bookingDto,
      @PathVariable Long id) {

    //TODO: test how save and update works and choose the best option
    Booking targetBooking = bookingService.findById(id);

    bookingService.populateFields(targetBooking, bookingDto);
    Booking savedBooking = bookingService.save(targetBooking);

    System.out.println("new booking: \n" + savedBooking);
    return ResponseEntity.ok(new BookingDto(savedBooking));
  }

  @PostMapping("bookings")
  public ResponseEntity<BookingDto> createBooking(@RequestBody BookingDto bookingDto) {

    Booking createdBooking = new Booking();

    bookingService.populateFields(createdBooking, bookingDto);
    Booking savedBooking = bookingService.save(createdBooking);

    return ResponseEntity.ok(new BookingDto(savedBooking));
  }
}
