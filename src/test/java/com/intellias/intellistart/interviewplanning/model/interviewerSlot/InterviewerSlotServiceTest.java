package com.intellias.intellistart.interviewplanning.model.interviewerSlot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.intellias.intellistart.interviewplanning.controllers.dtos.InterviewerSlotDto;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidDayOfWeekException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidInterviewerException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotIsOverlappingException;
import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotDtoValidator;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotRepository;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.period.PeriodRepository;
import com.intellias.intellistart.interviewplanning.model.period.PeriodService;
import com.intellias.intellistart.interviewplanning.model.user.Role;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.user.UserService;
import com.intellias.intellistart.interviewplanning.model.week.Week;


import com.intellias.intellistart.interviewplanning.model.week.WeekService;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

public class InterviewerSlotServiceTest {


  static InterviewerSlotRepository interviewerSlotRepository = Mockito.mock(
      InterviewerSlotRepository.class);

  @Autowired
  static UserService userService;
  @Autowired
  static WeekService weekService;
  @Autowired
  static PeriodService periodService;
  @Autowired
  static InterviewerSlotDtoValidator interviewerSlotDTOValidator =
      new InterviewerSlotDtoValidator(periodService, userService, weekService, interviewerSlotRepository);

  InterviewerSlotService cut = new InterviewerSlotService(
      interviewerSlotDTOValidator, interviewerSlotRepository
  );


  @Test
  void getSlotByIdTest(){
    when(cut.getSlotById(1L)).thenReturn(Optional.of(is1));
    InterviewerSlot actual = cut.getSlotById(1L).get();
    InterviewerSlot expected = is1;
    assertEquals(expected, actual);
  }


  @Test
  void createInterviewerSlotsTest() {
    InterviewerSlot expected = new InterviewerSlot(null, w1, DayOfWeek.TUE, p1, null, u1);
    cut.createInterviewerSlot(u1, w1, DayOfWeek.TUE, p1);
    ArgumentCaptor<InterviewerSlot> slotArgumentCaptor = ArgumentCaptor.forClass(
        InterviewerSlot.class);
    verify(interviewerSlotRepository).save(slotArgumentCaptor.capture());
    InterviewerSlot actual = slotArgumentCaptor.getValue();
    assertEquals(expected, actual);
  }

  static User u1 = new User(1L, "interviewer@gmail.com", Role.INTERVIEWER);
  static User u2 = new User(2L, "interviewer@gmail2.com", Role.INTERVIEWER);

  Week w1 = new Week(50L, new HashSet<>());
  static Week w2 = new Week(100L, new HashSet<>());
  static Week w3 = new Week(103L, new HashSet<>());
  Period p1 = new Period(null, LocalTime.of(10, 0), LocalTime.of(20, 0),
      new HashSet<>(), new HashSet<>(), new HashSet<>());
  static Period p2 = new Period(null, LocalTime.of(12, 0), LocalTime.of(18, 0),
      new HashSet<>(), new HashSet<>(), new HashSet<>());
  static Period p3 = new Period(null, LocalTime.of(11, 0), LocalTime.of(17, 30),
      new HashSet<>(), new HashSet<>(), new HashSet<>());
  InterviewerSlot is1 = new InterviewerSlot(null, w1, DayOfWeek.TUE, p1, null, u1);
  static InterviewerSlot is2 = new InterviewerSlot(null, w2, DayOfWeek.TUE, p2, null, u1);
  static InterviewerSlot is3 = new InterviewerSlot(null, w3, DayOfWeek.TUE, p3, null, u2);




}
