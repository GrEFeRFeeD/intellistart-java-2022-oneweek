package com.intellias.intellistart.interviewplanning.model.bookinglimit;

import com.intellias.intellistart.interviewplanning.model.booking.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for BookingLimit entity.
 */
@Service
public class BookingLimitService {

  private final BookingRepository bookingRepository;

  @Autowired
  public BookingLimitService(BookingRepository bookingRepository) {
    this.bookingRepository = bookingRepository;
  }
}
