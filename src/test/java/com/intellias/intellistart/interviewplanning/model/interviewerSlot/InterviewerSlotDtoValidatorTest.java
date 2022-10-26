package com.intellias.intellistart.interviewplanning.model.interviewerSlot;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.intellias.intellistart.interviewplanning.exceptions.CannotEditThisWeekException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidDayOfWeekException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidInterviewerException;
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
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;



public class InterviewerSlotDtoValidatorTest {

  static UserRepository userRepository = Mockito.mock(UserRepository.class);
  static UserService userService = new UserService(userRepository);

  static PeriodService periodService;
  static WeekService weekService;

  static InterviewerSlotRepository interviewerSlotRepository;
  InterviewerSlotDtoValidator cut = new InterviewerSlotDtoValidator(
      periodService, userService, weekService, interviewerSlotRepository
  );

  @Test
  void isCorrectDayTest() {
    assertThrows(InvalidDayOfWeekException.class, () -> cut.isCorrectDay("friday"));
    assertThrows(InvalidDayOfWeekException.class, () -> cut.isCorrectDay("february"));
    assertDoesNotThrow(() -> cut.isCorrectDay("TUE"));
  }

  @Test
  void isInterviewerRoleINTERVIEWERTest() {
    assertThrows(InvalidInterviewerException.class, () -> cut.isInterviewerRoleInterviewer(u3));
    assertThrows(InvalidInterviewerException.class, () -> cut.isInterviewerRoleInterviewer(u2));
    assertDoesNotThrow(() -> cut.isInterviewerRoleInterviewer(u1));
  }

  @Test
  void isInterviewerExistTest() {
    final Optional<User> user = Optional.empty();
    assertThrows(InvalidInterviewerException.class, () -> cut.isUserPresent(user));
    final Optional<User> user2 = Optional.of(u1);
    assertDoesNotThrow(() -> cut.isUserPresent(user2));
  }

  @Test
  void canEditThisWeekTest() {
    assertThrows(CannotEditThisWeekException.class, () -> cut.canEditThisWeek(is2));
    assertDoesNotThrow(() -> cut.canEditThisWeek(is1));
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
  static User u3 = new User(null, "interviewer3@gmail.com", Role.COORDINATOR);


  static Week w1 = new Week(100L, new HashSet<>());
  static Week w2 = new Week(35L, new HashSet<>());

  static Period p1 = new Period(null, LocalTime.of(10, 0), LocalTime.of(20, 0),
      new HashSet<>(), new HashSet<>(), new HashSet<>());
  static Period p2 = new Period(null, LocalTime.of(10, 0), LocalTime.of(20, 0),
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
