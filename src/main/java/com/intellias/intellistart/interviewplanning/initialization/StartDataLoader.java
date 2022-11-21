package com.intellias.intellistart.interviewplanning.initialization;

import com.intellias.intellistart.interviewplanning.model.bookinglimit.BookingLimit;
import com.intellias.intellistart.interviewplanning.model.bookinglimit.BookingLimitKey;
import com.intellias.intellistart.interviewplanning.model.bookinglimit.BookingLimitRepository;
import com.intellias.intellistart.interviewplanning.model.user.Role;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.user.UserRepository;
import com.intellias.intellistart.interviewplanning.model.week.Week;
import com.intellias.intellistart.interviewplanning.model.week.WeekRepository;
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
  private final WeekRepository weekRepository;
  private final BookingLimitRepository bookingLimitRepository;

  @Value("${first-coordinator-email}")
  private String email;

  @Autowired
  public StartDataLoader(UserRepository userRepository, WeekRepository weekRepository,
      BookingLimitRepository bookingLimitRepository) {
    this.userRepository = userRepository;
    this.weekRepository = weekRepository;
    this.bookingLimitRepository = bookingLimitRepository;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {

    User firstCoordinator = new User(null, email, Role.COORDINATOR);
    firstCoordinator = userRepository.save(firstCoordinator);

    User firstInterviewer = new User(null,"sonyakazanceva1331@gmail.com",Role.INTERVIEWER);
    firstInterviewer = userRepository.save(firstInterviewer);

    User secondInterviewer = new User(null,"email@gmail.com",Role.INTERVIEWER);
    secondInterviewer = userRepository.save(secondInterviewer);

    Week week = new Week(40L,new HashSet<>());
    week = weekRepository.save(week);

    BookingLimit bookingLimit = new BookingLimit(
        new BookingLimitKey(firstInterviewer.getId(), week.getId()), 17, firstInterviewer,
        week);
    bookingLimit = bookingLimitRepository.save(bookingLimit);


    // Add needed repositories to this class and save any information you want
    // First coordinator will be with email azofer77@gmail.com
  }
}