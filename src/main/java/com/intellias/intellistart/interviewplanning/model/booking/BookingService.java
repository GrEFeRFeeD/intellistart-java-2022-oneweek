package com.intellias.intellistart.interviewplanning.model.booking;

import com.intellias.intellistart.interviewplanning.exceptions.BookingNotFoundException;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for Booking entity.
 */
@Service
public class BookingService {

  private final BookingRepository bookingRepository;

  /**
   * Constructor.
   */
  @Autowired
  public BookingService(BookingRepository bookingRepository) {
    this.bookingRepository = bookingRepository;
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
   * Alias for method in {@link BookingRepository}.
   */
  public Booking save(Booking booking) {
    return bookingRepository.save(booking);
  }


  /**
   * Delete the given bookings from DB.
   *
   * @param bookings - bookings that need to be removed from the database.
   */
  public void deleteBookings(Set<Booking> bookings) {
    bookingRepository.deleteAll(bookings);
  }
}
