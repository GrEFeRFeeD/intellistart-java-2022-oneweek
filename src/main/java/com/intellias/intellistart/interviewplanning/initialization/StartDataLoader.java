package com.intellias.intellistart.interviewplanning.initialization;

import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlot;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlotRepository;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlotService;
import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotRepository;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService;
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
  private final InterviewerSlotService interviewerSlotService;
  private final PeriodService periodService;
  private final WeekService weekService;

  @Value("${first-coordinator-email}")
  private String email;

  @Autowired
  public StartDataLoader(UserRepository userRepository, InterviewerSlotRepository interviewerSlotRepository, CandidateSlotRepository candidateSlotRepository, UserService userService,
      InterviewerSlotService interviewerSlotService, PeriodService periodService,
      WeekService weekService) {
    this.userRepository = userRepository;
    this.interviewerSlotRepository = interviewerSlotRepository;
    this.candidateSlotRepository = candidateSlotRepository;
    this.userService = userService;
    this.interviewerSlotService = interviewerSlotService;
    this.periodService = periodService;

    this.weekService = weekService;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {

    User firstCoordinator = new User(null, email, Role.COORDINATOR);
    firstCoordinator = userRepository.save(firstCoordinator);

    User firstInterviewer = userService.grantRoleByEmail("testing@gmail.com", Role.INTERVIEWER);
    User secondInterviewer = userService.grantRoleByEmail("second@gmail.com", Role.INTERVIEWER);

    DayOfWeek dayOfWeek = DayOfWeek.TUE;
    Week next = weekService.getNextWeek();
    LocalDate date = weekService.convertToLocalDate(next.getId(), dayOfWeek);

    Period periodInterviewer1 = periodService.obtainPeriod("12:00", "18:00");
    Period periodCandidate1 = periodService.obtainPeriod("12:00", "21:30");

    InterviewerSlot interviewerSlot1 = new InterviewerSlot(null, next, dayOfWeek, periodInterviewer1, new HashSet<>(), firstInterviewer);
    InterviewerSlot interviewerSlot1Saved = interviewerSlotRepository.save(interviewerSlot1);

    CandidateSlot candidateSlot1 = new CandidateSlot(null, date, periodCandidate1, new HashSet<>(),
            "candidate1@gmail.com", "Maks");
    CandidateSlot candidateSlot1Saved = candidateSlotRepository.save(candidateSlot1);




    System.out.println("data is loaded");
  }
}