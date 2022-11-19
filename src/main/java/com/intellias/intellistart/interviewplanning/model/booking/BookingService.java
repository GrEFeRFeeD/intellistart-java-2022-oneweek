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
   * Finds Booking object in DB by id and returns it wrapped in Optional.
   *
   * @param id id of needed Booking object.
   * @return Optional&lt;Booking&gt; with persisted Booking objects if
   *          such object with given id exists and with null if not.
   */
  public Optional<Booking> findById(Long id) {
    return bookingRepository.findById(id);
  }

  /**
   * Finds all Booking objects in DB and returns Set of them.
   *
   * @return Set&lt;Booking&gt; set with all Booking objects from the DB.
   */
  public Set<Booking> findAll() {
    return (Set<Booking>) bookingRepository.findAll();
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
