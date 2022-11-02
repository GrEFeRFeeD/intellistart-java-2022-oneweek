package com.intellias.intellistart.interviewplanning.model.booking;

import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for Booking entity.
 */
@Service
public class BookingService {

  private final BookingRepository bookingRepository;

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
   * Created Booking object with by given parameters.
   *
   * @return persisted Booking object.
   */
  public Booking createBooking() { // TODO: implement creating Builder object as a Factory method
    return null;
  }

  /**
   * Saves and returns saved Booking object.
   *
   * @param booking object to save.
   * @return saved Booking object.
   */
  public Booking save(Booking booking) {
    return bookingRepository.save(booking);
  }

  /**
   * Deletes Booking object from DB.
   *
   * @param booking persisted Booking object that will be deleted.
   */
  public void delete(Booking booking) { // TODO: is that action is enough to delete booking?
    bookingRepository.delete(booking);
  }

  /**
   * Deletes Booking object from DB by id.
   *
   * @param id id of Booking object that will be deleted.
   */
  public void deleteById(Long id) {
    Optional<Booking> booking = bookingRepository.findById(id);
    if (booking.isPresent()) {
      delete(booking.get());
    } else {
      // throw smth
    }
  }
}
