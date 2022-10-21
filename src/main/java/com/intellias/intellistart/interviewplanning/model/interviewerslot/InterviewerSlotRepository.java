package com.intellias.intellistart.interviewplanning.model.interviewerslot;

import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.week.Week;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

/**
 * DAO for InterviewerSlot entity.
 */

public interface InterviewerSlotRepository extends CrudRepository<InterviewerSlot, Long> {

  List<InterviewerSlot> getInterviewerSlotsByUserAndWeekAndDayOfWeek(User user ,Week week, DayOfWeek dayOfWeek);
}
