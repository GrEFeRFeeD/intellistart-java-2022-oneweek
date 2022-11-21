package com.intellias.intellistart.interviewplanning.initialization;

import com.intellias.intellistart.interviewplanning.model.booking.Booking;
import com.intellias.intellistart.interviewplanning.model.booking.BookingRepository;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlot;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlotRepository;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.period.PeriodRepository;
import com.intellias.intellistart.interviewplanning.model.user.Role;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Class for initializing first coordinator in the system.
 */
@Component
public class StartDataLoader implements ApplicationRunner {

  private final UserRepository userRepository;
  private final CandidateSlotRepository candidateSlotRepository;
  private final PeriodRepository periodRepository;
  private final BookingRepository bookingRepository;

  @Value("${first-coordinator-email}")
  private String email;

  @Autowired
  public StartDataLoader(UserRepository userRepository,
                         CandidateSlotRepository candidateSlotRepository,
                         PeriodRepository periodRepository,
                         BookingRepository bookingRepository) {
    this.userRepository = userRepository;
    this.candidateSlotRepository = candidateSlotRepository;
    this.periodRepository = periodRepository;
    this.bookingRepository = bookingRepository;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {

    User firstCoordinator = new User(null, email, Role.COORDINATOR);
    User firstInterviewer = new User(null, "interviewer1@gmail.com", Role.INTERVIEWER);
    User secondInterviewer = new User(null, "interviewer2@gmail.com", Role.INTERVIEWER);
    firstCoordinator = userRepository.save(firstCoordinator);
    firstInterviewer = userRepository.save(firstInterviewer);
    secondInterviewer = userRepository.save(secondInterviewer);

    Period period = new Period();
    period.setFrom(LocalTime.of(10,30));
    period.setTo(LocalTime.of(14,30));

    CandidateSlot candidateSlot = new CandidateSlot();
    candidateSlot.setEmail("bielobrov.8864899@stud.op.edu.ua");
    candidateSlot.setPeriod(period);
    candidateSlot.setDate(LocalDate.of(2022,12,18));

    Booking booking = new Booking();
    booking.setCandidateSlot(candidateSlot);
    booking.setPeriod(period);
    booking.setDescription("java");
    booking.setSubject("java");

    candidateSlot.addBooking(new Booking());

    period = periodRepository.save(period);
    candidateSlot = candidateSlotRepository.save(candidateSlot);
    booking = bookingRepository.save(booking);
    // Add needed repositories to this class and save any information you want
    // First coordinator will be with email azofer77@gmail.com
  }
}