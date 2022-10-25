package com.intellias.intellistart.interviewplanning.initialization;

import com.intellias.intellistart.interviewplanning.model.booking.Booking;
import com.intellias.intellistart.interviewplanning.model.booking.BookingKey;
import com.intellias.intellistart.interviewplanning.model.booking.BookingRepository;
import com.intellias.intellistart.interviewplanning.model.bookinglimit.BookingLimit;
import com.intellias.intellistart.interviewplanning.model.bookinglimit.BookingLimitKey;
import com.intellias.intellistart.interviewplanning.model.bookinglimit.BookingLimitRepository;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlot;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlotRepository;
import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotRepository;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.period.PeriodRepository;
import com.intellias.intellistart.interviewplanning.model.user.Role;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.user.UserRepository;
import com.intellias.intellistart.interviewplanning.model.week.Week;
import com.intellias.intellistart.interviewplanning.model.week.WeekRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Class for initializing test data.
 * In that stage please use this class only as example for creating some test data for your tasks.
 * Don't repeat methods of creating, validating and saving as shown in this class,
 * they are incorrect, smells etc.
 */
@Component
public class StartDataLoader implements ApplicationRunner {

  @Autowired UserRepository userRepository;
  @Autowired BookingLimitRepository bookingLimitRepository;
  @Autowired WeekRepository weekRepository;
  @Autowired InterviewerSlotRepository interviewerSlotRepository;
  @Autowired PeriodRepository periodRepository;
  @Autowired CandidateSlotRepository candidateSlotRepository;
  @Autowired BookingRepository bookingRepository;

  @Override
  public void run(ApplicationArguments args) throws Exception {

    User u1 = new User(null, "interviewer@gmail.com", Role.INTERVIEWER);
    User u2 = new User(null, "interviewer2@gmail.com", Role.INTERVIEWER);
    u1.setId(u1.getId()); // Don't mind. Just for maven validation. Is smells.
    u2.setId(u2.getId()); // Don't mind. Just for maven validation. Is smells.
    User u3 = new User(null, "coordingator@gmail.com", Role.COORDINATOR);
    User u4 = new User(null, "candidate@gmail.com", null);
    u3.setId(u3.getId()); // Don't mind. Just for maven codestyle validation. Is smells.
    u4.setId(u4.getId()); // Don't mind. Just for maven codestyle validation. Is smells.

    Week w1 = new Week(40L, new HashSet<>());

    BookingLimit bl1 = new BookingLimit(new BookingLimitKey(), 5, u1, w1);
    bl1.setId(bl1.getId()); // Don't mind. Just for maven codestyle validation. Is smells.

    Period p1 = new Period(null, LocalTime.of(10, 0), LocalTime.of(20, 0),
        new HashSet<>(), new HashSet<>(), new HashSet<>());
    Period p2 = new Period(null, LocalTime.of(12, 0), LocalTime.of(18, 0),
        new HashSet<>(), new HashSet<>(), new HashSet<>());
    p1.setId(p1.getId()); // Don't mind. Just for maven codestyle validation. Is smells.
    p2.setId(p2.getId()); // Don't mind. Just for maven codestyle validation. Is smells.
    Period p3 = new Period(null, LocalTime.of(15, 0), LocalTime.of(18, 0),
        new HashSet<>(), new HashSet<>(), new HashSet<>());
    Period p4 = new Period(null, LocalTime.of(15, 30), LocalTime.of(17, 0),
        new HashSet<>(), new HashSet<>(), new HashSet<>());
    p3.setId(p3.getId()); // Don't mind. Just for maven codestyle validation. Is smells.
    p4.setId(p4.getId()); // Don't mind. Just for maven codestyle validation. Is smells.

    InterviewerSlot is1 = new InterviewerSlot(null, w1, DayOfWeek.THU, p1, new HashSet<>(), u1);
    p1.addInterviewerSlot(is1);

    InterviewerSlot is2 = new InterviewerSlot(null, w1, DayOfWeek.FRI, p2, new HashSet<>(), u1);
    p2.addInterviewerSlot(is2);

    CandidateSlot cs1 = new CandidateSlot(null, LocalDate.of(2022, 10, 6), p3, new HashSet<>(), u4);
    p3.addCandidateSlot(cs1);

    Booking b1 = new Booking(new BookingKey(), "Test subject", "Test description", is1, cs1, p4);
    is1.addBooking(b1);
    cs1.addBooking(b1);

    userRepository.save(u1);
    userRepository.save(u2);
    userRepository.save(u3);
    userRepository.save(u4);

    weekRepository.save(w1);

    bookingLimitRepository.save(bl1);

    periodRepository.save(p1);
    periodRepository.save(p2);
    periodRepository.save(p3);
    periodRepository.save(p4);

    interviewerSlotRepository.save(is1);
    interviewerSlotRepository.save(is2);

    candidateSlotRepository.save(cs1);

    bookingRepository.save(b1);

    u1 = userRepository.findById(u1.getId()).get();
    u2 = userRepository.findById(u2.getId()).get();
    u3 = userRepository.findById(u3.getId()).get();
    u4 = userRepository.findById(u4.getId()).get();

    w1 = weekRepository.findById(w1.getId()).get();

    bl1 = bookingLimitRepository.findById(bl1.getId()).get();

    p1 = periodRepository.findById(p1.getId()).get();
    p2 = periodRepository.findById(p2.getId()).get();
    p3 = periodRepository.findById(p3.getId()).get();
    p4 = periodRepository.findById(p4.getId()).get();

    is1 = interviewerSlotRepository.findById(is1.getId()).get();
    is2 = interviewerSlotRepository.findById(is2.getId()).get();

    cs1 = candidateSlotRepository.findById(cs1.getId()).get();

    b1 = bookingRepository.findById(b1.getId()).get();

    System.out.println("====== USERS ======");
    System.out.println(u1);
    System.out.println(u2);
    System.out.println(u3);
    System.out.println(u4);
    System.out.println("====== BOOKING_LIMITS ======");
    System.out.println(bl1);
    System.out.println("====== WEEKS ======");
    //System.out.println(w1);
    System.out.println("====== PERIODS ======");
    System.out.println(p1);
    System.out.println(p2);
    System.out.println(p3);
    System.out.println(p4);
    System.out.println("====== INTERVIEWER_SLOTS ======");
    System.out.println(is1);
    System.out.println(is2);
    System.out.println("====== CANDIDATE_SLOTS ======");
    System.out.println(cs1);
    System.out.println("====== BOOKINGS ======");
    System.out.println(b1);
    System.out.println("==================");
    System.out.println("If don't want to see these messages, than go and comment this class: "
        + this.getClass());
  }
}