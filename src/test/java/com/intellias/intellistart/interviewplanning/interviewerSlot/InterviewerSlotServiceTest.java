package com.intellias.intellistart.interviewplanning.interviewerSlot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.intellias.intellistart.interviewplanning.controllers.dto.InterviewerSlotDTO;
import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotRepository;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.user.Role;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.week.Week;


import java.time.LocalTime;
import java.util.HashSet;
import java.util.stream.Stream;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

public class InterviewerSlotServiceTest {
  InterviewerSlotRepository interviewerSlotRepository = Mockito.mock(InterviewerSlotRepository.class);
  InterviewerSlotService interviewerSlotService = Mockito.mock(InterviewerSlotService.class);
  //InterviewerSlotService interviewerSlotService = new InterviewerSlotService(interviewerSlotService, interviewerSlotRepository);


  @ParameterizedTest
  @ArgumentsSource(InterviewerSlotArgumentsProvider.class)
  void getNumberOfWeekTest(InterviewerSlotDTO interviewerSlotDTO,InterviewerSlot expect) throws Exception {
    InterviewerSlot result = InterviewerSlotService.interviewerSlotValidation(interviewerSlotDTO);
    assertEquals(expect.getWeek(),result.getWeek());
    assertEquals(expect.getPeriod(),result.getPeriod());
    assertEquals(expect.getDayOfWeek(),result.getDayOfWeek());
    assertEquals(expect.getBookings(),result.getBookings());

  }

  @ParameterizedTest
  @CsvSource({"THU, true", "yy, false", ", false", "Fri, true", "SUN, true"})
  void isCorrectDayTest(String dayOfWeek, boolean expect){
    boolean result = InterviewerSlotService.isCorrectDay(dayOfWeek);
    assertEquals(expect,result);
  }



  @ParameterizedTest
  @ArgumentsSource(UserArgumentsProvider.class)
  void isInterviewerRoleINTERVIEWER(User user,boolean expect) {
    boolean result = InterviewerSlotService.isInterviewerRoleINTERVIEWER(user);
    assertEquals(expect,result);
  }

  static class InterviewerSlotArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
      return Stream.of(
          Arguments.of(new InterviewerSlotDTO(null, 13L, "THU", "10:00", "20:00"),
              is1),
          Arguments.of(new InterviewerSlotDTO(null, 42L, "FRI", "12:00", "18:00"),
              is2)
      );
    }
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
  static User u1 = new User(null, "interviewer@gmail.com", Role.INTERVIEWER, null);
  static User u2 = new User(null, "interviewer@gmail.com", Role.COORDINATOR, null);
  static User u3 = new User(null, "interviewer@gmail.com", Role.INTERVIEWER, null);

  static Week w1 = new Week(13L, new HashSet<>());
  static Week w2 = new Week(42L, new HashSet<>());


  static Period p1 = new Period(null, LocalTime.of(10, 0), LocalTime.of(20, 0),
      new HashSet<>(), new HashSet<>(), new HashSet<>());
  static Period p2 = new Period(null, LocalTime.of(12, 0), LocalTime.of(18, 0),
      new HashSet<>(), new HashSet<>(), new HashSet<>());

  static InterviewerSlot is1 = new InterviewerSlot(null, w1, DayOfWeek.THU, p1, new HashSet<>(), u1);

  static InterviewerSlot is2 = new InterviewerSlot(null, w2, DayOfWeek.FRI, p2, new HashSet<>(), u1);

  }
