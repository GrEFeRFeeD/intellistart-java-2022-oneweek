package com.intellias.intellistart.interviewplanning.model.user;

import com.intellias.intellistart.interviewplanning.model.booking.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoordinatorController {

  private BookingService bookingService;

  @Autowired
  public CoordinatorController(BookingService bookingService) {
    this.bookingService = bookingService;
  }
}
