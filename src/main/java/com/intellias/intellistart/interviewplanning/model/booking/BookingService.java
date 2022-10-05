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

  public Optional<Booking> findById(BookingKey id) {
    return bookingRepository.findById(id);
  }

  public Set<Booking> findAll() {
    return (Set<Booking>) bookingRepository.findAll();
  }

  public Booking save(Booking booking) {
    return bookingRepository.save(booking);
  }

  public void delete(Booking booking) {
    bookingRepository.delete(booking);
  }

  public void deleteById(BookingKey id) {
    bookingRepository.deleteById(id);
  }
}
