package com.intellias.intellistart.interviewplanning.interviewerSlot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotRepository;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.period.PeriodRepository;
import com.intellias.intellistart.interviewplanning.model.user.Role;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.user.UserRepository;
import com.intellias.intellistart.interviewplanning.model.week.Week;


import com.intellias.intellistart.interviewplanning.model.week.WeekRepository;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class InterviewerSlotServiceTest {


  static InterviewerSlotRepository interviewerSlotRepository = Mockito.mock(InterviewerSlotRepository.class);
  static UserRepository userRepository =  Mockito.mock(UserRepository.class);
  static PeriodRepository periodRepository =  Mockito.mock(PeriodRepository.class);
  static WeekRepository weekRepository  = Mockito.mock(WeekRepository.class);

   InterviewerSlotService cut = new InterviewerSlotService(
        interviewerSlotRepository, userRepository,periodRepository, weekRepository
    );



  @Test
  void createInterviewerSlotsTest(){
    InterviewerSlot expected = new InterviewerSlot(null, w1, DayOfWeek.TUE, p1, null, u1);

    cut.createInterviewerSlot(u1, w1, DayOfWeek.TUE, p1);

    ArgumentCaptor<InterviewerSlot> slotArgumentCaptor = ArgumentCaptor.forClass(InterviewerSlot.class);
    verify(interviewerSlotRepository).save(slotArgumentCaptor.capture());
    InterviewerSlot actual = slotArgumentCaptor.getValue();
    assertEquals(expected, actual);

  }

  @Test
  void getListOfInterviewerSlotsTest(){
    List<InterviewerSlot> expected = new ArrayList<>();
    expected.add(is1);
    Mockito.when(interviewerSlotRepository.getInterviewerSlotsByUserAndWeekAndDayOfWeek(is1.getUser(),
        is1.getWeek(), is1.getDayOfWeek())).thenReturn(expected);
    List<InterviewerSlot> actual = cut.getInterviewerSlots(is1.getUser(),
        is1.getWeek(), is1.getDayOfWeek());
    Assertions.assertEquals(actual, expected);
  }

  User u1 = new User(1L, "interviewer@gmail.com", Role.INTERVIEWER, null);
  Week w1 = new Week(50L, new HashSet<>());
  Period p1 = new Period(null, LocalTime.of(10, 0), LocalTime.of(20, 0),
      new HashSet<>(), new HashSet<>(), new HashSet<>());
  InterviewerSlot is1 = new InterviewerSlot(null, w1, DayOfWeek.TUE, p1, null, u1);

}
