package com.intellias.intellistart.interviewplanning.model.booking;

import com.intellias.intellistart.interviewplanning.controllers.dto.BookingDto;
import com.intellias.intellistart.interviewplanning.exceptions.BookingNotFoundException;
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

  private final BookingRepository bookingRepository;
  private final PeriodService periodService;
  private final CandidateSlotService candidateSlotService;
  private final InterviewerSlotService interviewerSlotService;

  @Autowired
  public BookingService(
          BookingRepository bookingRepository,
          PeriodService periodService,
          CandidateSlotService candidateSlotService,
          InterviewerSlotService interviewerSlotService) {
    this.bookingRepository = bookingRepository;
    this.periodService = periodService;
    this.candidateSlotService = candidateSlotService;
    this.interviewerSlotService = interviewerSlotService;
  }

  public Booking findById(Long id) {
    return bookingRepository.findById(id).orElseThrow(BookingNotFoundException::new);
  }


  public Booking save(Booking booking) {
    return bookingRepository.save(booking);
  }

  public void populateFields(Booking booking, BookingDto bookingDto) {

    Period period = periodService.obtainPeriod(bookingDto.getFrom(), bookingDto.getTo());
    booking.setPeriod(period);

    String subject = booking.getSubject();
    booking.setSubject(subject);

    String description = booking.getDescription();
    booking.setDescription(description);

    CandidateSlot candidateSlot = candidateSlotService
        .findById(bookingDto.getCandidateSlotId());
    booking.setCandidateSlot(candidateSlot);

    InterviewerSlot interviewerSlot = interviewerSlotService
        .findById(bookingDto.getInterviewerSlotId());
    booking.setInterviewerSlot(interviewerSlot);
  }
}
