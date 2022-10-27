package com.intellias.intellistart.interviewplanning.model.candidateslot.validation;

import com.intellias.intellistart.interviewplanning.exceptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotIsBookedException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotIsOverlappingException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotNotFoundException;
import com.intellias.intellistart.interviewplanning.model.booking.Booking;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlot;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlotService;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.period.PeriodService;
import com.intellias.intellistart.interviewplanning.model.user.Role;
import com.intellias.intellistart.interviewplanning.model.user.User;
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

public class CandidateSlotValidatorTest {

  private static CandidateSlotService candidateSlotService;
  private static CandidateSlotValidator cut;
  private static PeriodService periodService;
  private static CandidateSlot candidateSlot1;
  private static CandidateSlot candidateSlot2;
  private static CandidateSlot candidateSlot3;
  private static CandidateSlot candidateSlot4;
  private static CandidateSlot candidateSlot5;
  private static CandidateSlot candidateSlot6;
  private static CandidateSlot candidateSlot7;
  private static CandidateSlot candidateSlot8;

  @BeforeAll
  static void initialize() {
    candidateSlotService = Mockito.mock(CandidateSlotService.class);
    periodService = Mockito.mock(PeriodService.class);
    cut = new CandidateSlotValidator(candidateSlotService,
        periodService);

    Period period1 = new Period();
    period1.setFrom(LocalTime.of(9,0));
    period1.setTo(LocalTime.of(10,30));

    Period period2 = new Period();
    period2.setFrom(LocalTime.of(14,0));
    period2.setTo(LocalTime.of(20,30));

    User user1 = new User();
    user1.setEmail("test@unit.com");
    user1.setRole(Role.INTERVIEWER);

    candidateSlot1 = new CandidateSlot();
    candidateSlot1.setId(1L);
    candidateSlot1.setDate(LocalDate.of(1990,1,1));
    candidateSlot1.setPeriod(period1);
    candidateSlot1.setUser(user1);

    candidateSlot2 = new CandidateSlot();
    candidateSlot2.setId(2L);
    candidateSlot2.setDate(LocalDate.of(2021, 4, 24));
    candidateSlot2.setPeriod(period2);
    candidateSlot2.setUser(user1);

    candidateSlot3 = new CandidateSlot();
    candidateSlot3.setId(3L);
    candidateSlot3.setDate(LocalDate.of(LocalDate.now().getYear()+1,1,1));
    candidateSlot3.setPeriod(period1);
    candidateSlot3.setUser(user1);

    candidateSlot4 = new CandidateSlot();
    candidateSlot4.setId(4L);
    candidateSlot4.setDate(LocalDate.of(LocalDate.now().getYear()+1,2,2));
    candidateSlot4.setPeriod(period2);
    candidateSlot4.setUser(user1);

    candidateSlot5 = new CandidateSlot();
    candidateSlot5.setId(5L);
    candidateSlot5.setDate(LocalDate.of(LocalDate.now().getYear()+1,1,1));
    candidateSlot5.setPeriod(period2);
    candidateSlot5.setUser(user1);
    candidateSlot5.addBooking(new Booking());

    candidateSlot6 = new CandidateSlot();
    candidateSlot6.setId(6L);
    candidateSlot6.setDate(LocalDate.of(LocalDate.now().getYear()+1,2,2));
    candidateSlot6.setPeriod(period1);
    candidateSlot6.setUser(user1);
    candidateSlot6.addBooking(new Booking());

    candidateSlot7 = new CandidateSlot();
    candidateSlot7.setId(7L);
    candidateSlot7.setDate(LocalDate.of(LocalDate.now().getYear()+1,12,1));
    candidateSlot7.setPeriod(period1);
    candidateSlot7.setUser(user1);

    candidateSlot8 = new CandidateSlot();
    candidateSlot8.setId(8L);
    candidateSlot8.setDate(LocalDate.of(LocalDate.now().getYear()+1,9,2));
    candidateSlot8.setPeriod(period2);
    candidateSlot8.setUser(user1);
  }

  static Arguments[] validateCreateCandidateSlotArgs(){
    return new Arguments[]{
        Arguments.arguments(candidateSlot4),
        Arguments.arguments(candidateSlot3)
    };
  }
  @ParameterizedTest
  @MethodSource("validateCreateCandidateSlotArgs")
  void validateCreateCandidateSlotTest(CandidateSlot candidateSlot)
      throws SlotIsOverlappingException, InvalidBoundariesException {
    Mockito.when(candidateSlotService.getCandidateSlotsByUserAndDate(candidateSlot.getDate()))
        .thenReturn(List.of());

    cut.validateCreating(candidateSlot);

    Mockito.verify(candidateSlotService).getCandidateSlotsByUserAndDate(candidateSlot.getDate());
  }

  static Arguments[] validateCreateCandidateSlotExc1Args(){
    return new Arguments[]{
        Arguments.arguments(candidateSlot1, InvalidBoundariesException.class),
        Arguments.arguments(candidateSlot2, InvalidBoundariesException.class)
    };
  }
  @ParameterizedTest
  @MethodSource("validateCreateCandidateSlotExc1Args")
  void validateCreateCandidateSlotInvalidBoundariesExceptionTest(CandidateSlot candidateSlot,
      Class<Exception> actual) {
    Assertions.assertThrows(actual,
        ()-> cut.validateCreating(candidateSlot));
  }

  static Arguments[] validateCreateCandidateSlotExc2Args(){
    return new Arguments[]{
        Arguments.arguments(candidateSlot3, SlotIsOverlappingException.class,
            List.of(candidateSlot5, candidateSlot6)),
        Arguments.arguments(candidateSlot4, SlotIsOverlappingException.class,
            List.of(candidateSlot6, candidateSlot5))
    };
  }
  @ParameterizedTest
  @MethodSource("validateCreateCandidateSlotExc2Args")
  void validateCreateCandidateSlotSlotIsOverlappingExceptionTest(CandidateSlot candidateSlot,
      Class<Exception> actual, List<CandidateSlot> candidateSlotList) {
    Mockito.when(candidateSlotService.getCandidateSlotsByUserAndDate(candidateSlot.getDate()))
            .thenReturn(candidateSlotList);

    Period period = candidateSlot.getPeriod();
    Mockito.when(periodService.isOverlap(period, candidateSlotList.get(1).getPeriod()))
            .thenReturn(true);

    Assertions.assertThrows(actual,
        ()-> cut.validateCreating(candidateSlot));
  }

  static Arguments[] validateUpdateCandidateSlotArgs(){
    return new Arguments[]{
        Arguments.arguments(candidateSlot7, 3),
        Arguments.arguments(candidateSlot8, 2)
    };
  }
  @ParameterizedTest
  @MethodSource("validateUpdateCandidateSlotArgs")
  void validateUpdateCandidateSlotTest(CandidateSlot candidateSlot, long id)
      throws SlotNotFoundException, SlotIsOverlappingException, SlotIsBookedException,
      InvalidBoundariesException {
    Mockito.when(candidateSlotService.getCandidateSlotsByUserAndDate(candidateSlot.getDate()))
        .thenReturn(List.of());
    Mockito.when(candidateSlotService.getCandidateSlotById(id))
        .thenReturn(Optional.of(candidateSlot));

    cut.validateUpdating(candidateSlot, id);

    Mockito.verify(candidateSlotService).getCandidateSlotsByUserAndDate(candidateSlot.getDate());
    Mockito.verify(candidateSlotService).getCandidateSlotById(id);
  }

  static Arguments[] validateUpdateCandidateSlotExc1Args(){
    return new Arguments[]{
        Arguments.arguments(candidateSlot5, SlotNotFoundException.class, 4),
        Arguments.arguments(candidateSlot6, SlotNotFoundException.class, 5)
    };
  }
  @ParameterizedTest
  @MethodSource("validateUpdateCandidateSlotExc1Args")
  void validateUpdateCandidateSlotSlotNotFoundExceptionTest(CandidateSlot candidateSlot,
      Class<Exception> actual, long id) {
    Mockito.when(candidateSlotService.getCandidateSlotById(id)).thenReturn(Optional.empty());

    Assertions.assertThrows(actual,
        ()-> cut.validateUpdating(candidateSlot, id));
  }

  static Arguments[] validateUpdateCandidateSlotExc2Args(){
    return new Arguments[]{
        Arguments.arguments(candidateSlot5, SlotIsBookedException.class, 4),
        Arguments.arguments(candidateSlot6, SlotIsBookedException.class, 5)
    };
  }
  @ParameterizedTest
  @MethodSource("validateUpdateCandidateSlotExc2Args")
  void validateUpdateCandidateSlotSlotIsBookedExceptionTest(CandidateSlot candidateSlot,
      Class<Exception> actual, long id) {
    Mockito.when(candidateSlotService.getCandidateSlotById(id))
        .thenReturn(Optional.of(candidateSlot));

    Assertions.assertThrows(actual,
        ()-> cut.validateUpdating(candidateSlot, id));
  }
}
