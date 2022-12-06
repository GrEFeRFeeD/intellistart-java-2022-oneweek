package com.intellias.intellistart.interviewplanning.initialization;

import com.intellias.intellistart.interviewplanning.model.booking.Booking;
import com.intellias.intellistart.interviewplanning.model.booking.BookingService;
import com.intellias.intellistart.interviewplanning.model.bookinglimit.BookingLimitService;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlot;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlotRepository;
import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotRepository;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.period.PeriodService;
import com.intellias.intellistart.interviewplanning.model.user.Role;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.user.UserRepository;
import com.intellias.intellistart.interviewplanning.model.user.UserService;
import com.intellias.intellistart.interviewplanning.model.week.Week;
import com.intellias.intellistart.interviewplanning.model.week.WeekService;
import java.time.LocalDate;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Class for initializing first coordinator in the system.
 */
@Component
public class StartDataLoader implements ApplicationRunner {

  private final UserRepository userRepository;
  private final InterviewerSlotRepository interviewerSlotRepository;
  private final CandidateSlotRepository candidateSlotRepository;
  private final UserService userService;
  private final BookingService bookingService;
  private final PeriodService periodService;
  private final WeekService weekService;
  private final BookingLimitService bookingLimitService;

  @Value("${first-coordinator-email}")
  private String email;

  /**
   * Initial data load.
   */
  @Autowired
  public StartDataLoader(
      UserRepository userRepository, InterviewerSlotRepository interviewerSlotRepository,
      CandidateSlotRepository candidateSlotRepository, UserService userService,
      BookingService bookingService, PeriodService periodService,
      WeekService weekService, BookingLimitService bookingLimitService) {
    this.userRepository = userRepository;
    this.interviewerSlotRepository = interviewerSlotRepository;
    this.candidateSlotRepository = candidateSlotRepository;
    this.userService = userService;
    this.bookingService = bookingService;
    this.periodService = periodService;
    this.weekService = weekService;
    this.bookingLimitService = bookingLimitService;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {

    User firstCoordinator = new User(null, email, Role.COORDINATOR);
    firstCoordinator = userRepository.save(firstCoordinator);

    User firstInterviewer = userService.grantRoleByEmail("testing@gmail.com", Role.INTERVIEWER);

    Week next = weekService.getNextWeek();

    Period periodInterviewer1 = periodService.obtainPeriod("12:00", "18:00");
    Period periodCandidate1 = periodService.obtainPeriod("12:00", "21:30");

    DayOfWeek dayOfWeek1 = DayOfWeek.TUE;
    LocalDate date1 = weekService.convertToLocalDate(next.getId(), dayOfWeek1);

    InterviewerSlot interviewerSlot1 = new InterviewerSlot(null, next, dayOfWeek1,
        periodInterviewer1, new HashSet<>(), firstInterviewer);
    interviewerSlot1 = interviewerSlotRepository.save(interviewerSlot1);

    DayOfWeek dayOfWeek2 = DayOfWeek.THU;
    LocalDate date2 = weekService.convertToLocalDate(next.getId(), dayOfWeek2);

    InterviewerSlot interviewerSlot2 = new InterviewerSlot(null, next, dayOfWeek2,
        periodInterviewer1, new HashSet<>(), firstInterviewer);
    interviewerSlot2 = interviewerSlotRepository.save(interviewerSlot2);

    CandidateSlot candidateSlot1 = new CandidateSlot(null, date1, periodCandidate1,
        new HashSet<>(), "candidate1@gmail.com", "Maks");
    candidateSlot1 = candidateSlotRepository. save(candidateSlot1);

    CandidateSlot candidateSlot2 = new CandidateSlot(null, date2, periodCandidate1,
        new HashSet<>(), "candidate2@gmail.com", "Margarita");
    candidateSlot2 = candidateSlotRepository.save(candidateSlot2);

    Period bookingPeriod1 = periodService.obtainPeriod("16:00", "17:30");

    Booking booking1 = new Booking(null, "interview", "Spider-man",
            interviewerSlot2, candidateSlot2, bookingPeriod1);

    booking1 = bookingService.save(booking1);

    bookingLimitService.createBookingLimit(firstInterviewer, 2);

    System.out.println("data is loaded");
  }
}