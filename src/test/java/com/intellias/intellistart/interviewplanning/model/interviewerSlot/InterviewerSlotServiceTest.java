package com.intellias.intellistart.interviewplanning.model.interviewerSlot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotDtoValidator;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotRepository;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.period.PeriodRepository;
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
  static PeriodRepository periodRepository = Mockito.mock(PeriodRepository.class);
  static InterviewerSlotDtoValidator interviewerSlotDTOValidator;

  InterviewerSlotService cut = new InterviewerSlotService(
      interviewerSlotDTOValidator, interviewerSlotRepository, periodRepository
  );


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

  User u1 = new User(1L, "interviewer@gmail.com", Role.INTERVIEWER);
  Week w1 = new Week(50L, new HashSet<>());
  Period p1 = new Period(null, LocalTime.of(10, 0), LocalTime.of(20, 0),
      new HashSet<>(), new HashSet<>(), new HashSet<>());
  InterviewerSlot is1 = new InterviewerSlot(null, w1, DayOfWeek.TUE, p1, null, u1);

}
