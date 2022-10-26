package com.intellias.intellistart.interviewplanning.model.interviewerSlot;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.intellias.intellistart.interviewplanning.controllers.dtos.InterviewerSlotDto;
import com.intellias.intellistart.interviewplanning.exceptions.CannotEditThisWeekException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidDayOfWeekException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidInterviewerException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotIsOverlappingException;
import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotDtoValidator;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotRepository;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.period.PeriodRepository;
import com.intellias.intellistart.interviewplanning.model.period.PeriodService;
import com.intellias.intellistart.interviewplanning.model.period.services.OverlapService;
import com.intellias.intellistart.interviewplanning.model.period.services.TimeConverter;
import com.intellias.intellistart.interviewplanning.model.period.services.validation.PeriodValidator;
import com.intellias.intellistart.interviewplanning.model.user.Role;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.user.UserRepository;
import com.intellias.intellistart.interviewplanning.model.user.UserService;
import com.intellias.intellistart.interviewplanning.model.week.Week;
import com.intellias.intellistart.interviewplanning.model.week.WeekRepository;
import com.intellias.intellistart.interviewplanning.model.week.WeekService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;


public class InterviewerSlotDtoValidatorTest {

  static UserRepository userRepository = Mockito.mock(UserRepository.class);
  @MockBean
  static UserService userService = new UserService(userRepository);
  static PeriodRepository periodRepository = Mockito.mock(PeriodRepository.class);
  static TimeConverter timeConverter = new TimeConverter();
  static PeriodValidator periodValidator = new PeriodValidator();
  static OverlapService overlapService = new OverlapService();
  @MockBean
  static PeriodService periodService = new PeriodService(periodRepository, timeConverter,
      periodValidator, overlapService);
  static WeekRepository weekRepository = Mockito.mock(WeekRepository.class);
  @MockBean
  static WeekService weekService = new WeekService(weekRepository);




  static InterviewerSlotRepository interviewerSlotRepository =
      Mockito.mock(InterviewerSlotRepository.class);
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
    when(weekService.getCurrentWeek()).thenReturn(new Week(43L,new HashSet<>()));
    assertThrows(CannotEditThisWeekException.class, () -> cut.canEditThisWeek(is2));
    assertDoesNotThrow(() -> cut.canEditThisWeek(is1));
  }

  @Test
  void isSlotOverlapTest() {
    List<InterviewerSlot> list = new ArrayList<>();
    when(interviewerSlotRepository
        .getInterviewerSlotsByUserIdAndWeekIdAndDayOfWeek(u1.getId(),
            w1.getId(), DayOfWeek.TUE)).thenReturn(list);
    assertDoesNotThrow(() -> cut.isSlotOverlapping(p1, w1, u1, DayOfWeek.TUE));
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

  //TODO clear these arguments
  static User u1 = new User(null, "interviewer@gmail.com", Role.INTERVIEWER);

  static User u2 = new User(null, "interviewer2@gmail.com", Role.COORDINATOR);
  static User u3 = new User(null, "interviewer3@gmail.com", Role.COORDINATOR);


  static Week w1 = new Week(100L, new HashSet<>());
  static Week w2 = new Week(35L, new HashSet<>());
  static Week w3 = new Week(100L, new HashSet<>());

  static Period p1 = new Period(null, LocalTime.of(10, 0), LocalTime.of(20, 0),
      new HashSet<>(), new HashSet<>(), new HashSet<>());
  static Period p2 = new Period(null, LocalTime.of(16, 0), LocalTime.of(20, 0),
      new HashSet<>(), new HashSet<>(), new HashSet<>());
  static InterviewerSlot is1 = new InterviewerSlot(null, w1, DayOfWeek.THU, p1, new HashSet<>(),
      u1);
  static InterviewerSlot is2 = new InterviewerSlot(null, w2, DayOfWeek.FRI, p1, new HashSet<>(),
      u1);
  static InterviewerSlot is3 = new InterviewerSlot(null, w2, DayOfWeek.TUE, p1, new HashSet<>(),
      u2);
  static InterviewerSlot is4 = new InterviewerSlot(null, w3, DayOfWeek.SAT, p2, new HashSet<>(),
      u2);

  static InterviewerSlotDto dto2 = new InterviewerSlotDto(1L, 100L,
      "TUE", "12:00", "18:00");
  static InterviewerSlotDto dto3 = new InterviewerSlotDto(1L, 103L,
      "THU", "11:00", "17:30");

  static User u11 = new User(1L, "interviewer@gmail.com", Role.INTERVIEWER);
  static User u22 = new User(2L, "interviewer@gmail2.com", Role.INTERVIEWER);

  static Week w22 = new Week(100L, new HashSet<>());
  static Week w33 = new Week(103L, new HashSet<>());
  static Period p22 = new Period(null, LocalTime.of(12, 0), LocalTime.of(18, 0),
      new HashSet<>(), new HashSet<>(), new HashSet<>());
  static Period p33 = new Period(null, LocalTime.of(11, 0), LocalTime.of(17, 30),
      new HashSet<>(), new HashSet<>(), new HashSet<>());
  static InterviewerSlot is22 = new InterviewerSlot(null, w22, DayOfWeek.TUE, p22, null, u11);
  static InterviewerSlot is33 = new InterviewerSlot(null, w33, DayOfWeek.THU, p33, null, u22);


  static class DtoSlotArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
      return Stream.of(
          Arguments.of(dto2, is22),
          Arguments.of(dto3, is33)
      );
    }
  }

  @ParameterizedTest
  @ArgumentsSource(DtoSlotArgumentsProvider.class)
  void InterviewerSlotValidationIdTest(InterviewerSlotDto dto, InterviewerSlot expected)
      throws InvalidDayOfWeekException, SlotIsOverlappingException, InvalidInterviewerException {
    when(userService.getUserById(1L)).thenReturn(Optional.of(expected.getUser()));
    when(periodService.getPeriod(dto.getFrom(), dto.getTo())).thenReturn(expected.getPeriod());
    when(weekService.getWeekByWeekNum(dto.getWeek())).thenReturn(expected.getWeek());
    when(weekService.getCurrentWeek()).thenReturn(new Week(20L, new HashSet<>()));
    InterviewerSlot actual = cut.interviewerSlotValidateDto(dto);
    assertEquals(actual.getWeek(), expected.getWeek());
    assertEquals(actual.getDayOfWeek(), expected.getDayOfWeek());
  }

}