package com.intellias.intellistart.interviewplanning.model.interviewerSlot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotDtoValidator;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotRepository;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.period.PeriodService;
import com.intellias.intellistart.interviewplanning.model.user.Role;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.user.UserRepository;
import com.intellias.intellistart.interviewplanning.model.user.UserService;
import com.intellias.intellistart.interviewplanning.model.week.Week;
import com.intellias.intellistart.interviewplanning.model.week.WeekService;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;


public class InterviewerSlotDtoValidatorTest {

  static UserRepository userRepository = Mockito.mock(UserRepository.class);
  static UserService userService = new UserService(userRepository);

  static PeriodService periodService;
  static WeekService weekService;

  static InterviewerSlotRepository interviewerSlotRepository;
  InterviewerSlotDtoValidator interviewerSlotDTOValidator = new InterviewerSlotDtoValidator(
      periodService, userService, weekService, interviewerSlotRepository
  );





  @ParameterizedTest
  @CsvSource({"THU, true", "yy, false", ", false", "Fri, true", "SUN, true"})
  void isCorrectDayTest(String dayOfWeek, boolean expect) {
    boolean actual = interviewerSlotDTOValidator.isCorrectDay(dayOfWeek);
    assertEquals(expect, actual);
  }


  @ParameterizedTest
  @ArgumentsSource(UserArgumentsProvider.class)
  void isInterviewerRoleINTERVIEWER(User user, boolean expect) {
    boolean actual = interviewerSlotDTOValidator.isInterviewerRoleInterviewer(user);
    assertEquals(expect, actual);
  }

  static class UserArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
      return Stream.of(
          Arguments.of(u1, true),
          Arguments.of(u2, false),
          Arguments.of(u3, true)
      );
    }
  }

  static User u1 = new User(null, "interviewer@gmail.com", Role.INTERVIEWER);

  static User u2 = new User(null, "interviewer2@gmail.com", Role.COORDINATOR);
  static User u3 = new User(null, "interviewer3@gmail.com", Role.INTERVIEWER);


  static Week w1 = new Week(45L, new HashSet<>());
  static Week w2 = new Week(47L, new HashSet<>());

  static Period p1 = new Period(null, LocalTime.of(10, 0), LocalTime.of(20, 0),
      new HashSet<>(), new HashSet<>(), new HashSet<>());
  static InterviewerSlot is1 = new InterviewerSlot(null, w1, DayOfWeek.THU, p1, new HashSet<>(),
      u1);
  static InterviewerSlot is2 = new InterviewerSlot(null, w2, DayOfWeek.FRI, p1, new HashSet<>(),
      u1);
  static InterviewerSlot is3 = new InterviewerSlot(null, w2, DayOfWeek.TUE, p1, new HashSet<>(),
      u2);
  static InterviewerSlot is4 = new InterviewerSlot(null, w1, DayOfWeek.SAT, p1, new HashSet<>(),
      u2);


}
