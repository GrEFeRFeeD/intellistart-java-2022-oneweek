package com.intellias.intellistart.interviewplanning.model.interviewerSlot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class InterviewerSlotServiceTest {


  static InterviewerSlotRepository interviewerSlotRepository = Mockito.mock(
      InterviewerSlotRepository.class);

  InterviewerSlotService cut = new InterviewerSlotService(
      interviewerSlotRepository
  );


  @Test
  void getSlotByIdTest(){
    when(cut.findById(1L)).thenReturn(is1);
    InterviewerSlot actual = cut.findById(1L);
    InterviewerSlot expected = is1;
    assertEquals(expected, actual);
  }

  @Test
  void createInterviewerSlotsTest() {
    InterviewerSlot expected = new InterviewerSlot(null, w1, DayOfWeek.TUE, p1, null, u1);
    cut.create(expected);
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
