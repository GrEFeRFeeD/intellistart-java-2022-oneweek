package com.intellias.intellistart.interviewplanning.model.candidateslot;

import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.period.PeriodService;
import com.intellias.intellistart.interviewplanning.model.user.Role;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.user.UserService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

public class CandidateSlotServiceTest {

  private static CandidateSlotRepository candidateSlotRepository;
  private static PeriodService periodService;
  private static UserService userService;
  private static CandidateSlotService cut;
  private static CandidateSlot candidateSlot1;
  private static CandidateSlot candidateSlot2;
  private static Period period1;
  private static Period period2;
  private static User user1;

  @BeforeAll
  static void initialize() {
    candidateSlotRepository = Mockito.mock(CandidateSlotRepository.class);
    periodService = Mockito.mock(PeriodService.class);
    userService = Mockito.mock(UserService.class);
    cut = new CandidateSlotService(candidateSlotRepository,
        periodService, userService);
    period1 = new Period();
    period1.setFrom(LocalTime.of(9,0));
    period1.setTo(LocalTime.of(10,30));
    period2 = new Period();
    period2.setFrom(LocalTime.of(14,0));
    period2.setTo(LocalTime.of(20,30));
    user1 = new User();
    user1.setEmail("test@unit.com");
    user1.setRole(Role.INTERVIEWER);

    candidateSlot1 = new CandidateSlot();
    candidateSlot1.setDate(LocalDate.of(2023, 1, 1));
    candidateSlot1.setPeriod(period1);
    candidateSlot1.setUser(user1);

    candidateSlot2 = new CandidateSlot();
    candidateSlot2.setDate(LocalDate.of(2023, 4, 24));
    candidateSlot2.setPeriod(period2);
    candidateSlot2.setUser(user1);
  }

  static Arguments[] createTestArgs(){
    return new Arguments[]{
        Arguments.arguments(candidateSlot1),
        Arguments.arguments(candidateSlot2)
    };
  }
  @ParameterizedTest
  @MethodSource("createTestArgs")
  void createTest(CandidateSlot expected) {
    Mockito.when(candidateSlotRepository.save(expected)).thenReturn(expected);

    CandidateSlot actual = cut.create(expected);
    Assertions.assertEquals(actual, expected);
  }

  static Arguments[] updateTestArgs(){
    return new Arguments[]{
        Arguments.arguments(candidateSlot1, 1l),
        Arguments.arguments(candidateSlot2, 4l)
    };
  }
  @ParameterizedTest
  @MethodSource("updateTestArgs")
  void updateTest(CandidateSlot expected, Long id) {
    Mockito.when(candidateSlotRepository.save(expected)).thenReturn(expected);

    CandidateSlot actual = cut.update(expected, id);
    Assertions.assertEquals(actual, expected);
    Assertions.assertEquals(id, expected.getId());
  }

  static Arguments[] getAllSlotsOfCandidateArgs(){
    return new Arguments[]{
        Arguments.arguments(List.of()),
        Arguments.arguments(List.of(candidateSlot1, candidateSlot2))
    };
  }
  @ParameterizedTest
  @MethodSource("getAllSlotsOfCandidateArgs")
  void getAllSlotsOfCandidateTest(List<CandidateSlot> expected) {
    Mockito.when(userService.getCurrentUser()).thenReturn(user1);
    Mockito.when(candidateSlotRepository.findByUser(userService.getCurrentUser()))
        .thenReturn(expected);

    List<CandidateSlot> actual = cut.getAllSlotsOfCandidate();
    Assertions.assertEquals(actual, expected);
  }

  static Arguments[] getCandidateSlotsByUserAndDateArgs(){
    return new Arguments[]{
        Arguments.arguments(List.of(), LocalDate.of(2022,1,1)),
        Arguments.arguments(List.of(candidateSlot1), LocalDate.of(2023, 1, 1)),
        Arguments.arguments(List.of(candidateSlot2), LocalDate.of(2023, 4, 24))
    };
  }
  @ParameterizedTest
  @MethodSource("getCandidateSlotsByUserAndDateArgs")
  void getCandidateSlotsByUserAndDateTest(List<CandidateSlot> expected, LocalDate date) {
    Mockito.when(userService.getCurrentUser()).thenReturn(user1);
    Mockito.when(candidateSlotRepository.findByUserAndDate(userService.getCurrentUser(), date))
        .thenReturn(expected);

    List<CandidateSlot> actual = cut.getCandidateSlotsByUserAndDate(date);
    Assertions.assertEquals(actual, expected);
  }

  static Arguments[] getCandidateSlotByIdArgs(){
    return new Arguments[]{
        Arguments.arguments(candidateSlot1, 1l),
        Arguments.arguments(candidateSlot2, 2l),
    };
  }
  @ParameterizedTest
  @MethodSource("getCandidateSlotByIdArgs")
  void getCandidateSlotByIdTest(CandidateSlot expected, Long id) {
    Mockito.when(candidateSlotRepository.findById(id)).thenReturn(Optional.of(expected));

    Optional<CandidateSlot> optional = cut.getCandidateSlotById(id);
    CandidateSlot actual = optional.orElse(null);
    Assertions.assertEquals(actual, expected);
  }

  static Arguments[] createCandidateSlotArgs(){
    return new Arguments[]{
        Arguments.arguments(LocalDate.of(2023, 1, 1),
            "9:00","10:30", candidateSlot1, period1),
        Arguments.arguments(LocalDate.of(2023, 4, 24),
            "14:00","20:30", candidateSlot1, period2),
    };
  }
  @ParameterizedTest
  @MethodSource("createCandidateSlotArgs")
  void createCandidateSlotTest(LocalDate date, String from, String to,
      CandidateSlot expected, Period period) {
    Mockito.when(periodService.getPeriod(from, to)).thenReturn(period);
    Mockito.when(userService.getCurrentUser()).thenReturn(user1);

    CandidateSlot actual = cut.createCandidateSlot(date, from, to);
    Assertions.assertEquals(actual, expected);
  }
}
