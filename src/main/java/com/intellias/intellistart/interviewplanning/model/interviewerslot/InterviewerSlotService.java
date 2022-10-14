package com.intellias.intellistart.interviewplanning.model.interviewerslot;

import com.intellias.intellistart.interviewplanning.controllers.dto.InterviewerSlotDTO;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidDayOfWeekException;
import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.user.Role;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.week.Week;
import java.time.LocalTime;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


/**
 * Service for InterviewSlot entity.
 */
@Service
public class InterviewerSlotService {

  @Autowired
  public InterviewerSlotService(InterviewerSlotRepository interviewerSlotRepository) {
  }

  public static InterviewerSlot interviewerSlotValidation(InterviewerSlotDTO interviewerSlotDTO) throws Exception {

    if (!ObjectUtils.containsConstant(DayOfWeek.values(), interviewerSlotDTO.getDayOfWeek())) {
      throw new InvalidDayOfWeekException();
    }
    Period period = Period.getPeriod(interviewerSlotDTO.getFrom(), interviewerSlotDTO.getTo()); // validate from to

    Week week = new Week(interviewerSlotDTO.getWeek(), new HashSet<>()); // getWeek
    DayOfWeek dayOfWeek = DayOfWeek.valueOf(interviewerSlotDTO.getDayOfWeek());
    User u1 = new User(null, "interviewer@gmail.com", Role.INTERVIEWER, null);

    return new InterviewerSlot(null, week, dayOfWeek, period, new HashSet<>(), u1);


  }


}
