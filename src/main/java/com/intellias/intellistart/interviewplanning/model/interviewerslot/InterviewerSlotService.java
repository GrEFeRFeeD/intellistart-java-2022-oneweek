package com.intellias.intellistart.interviewplanning.model.interviewerslot;

import com.intellias.intellistart.interviewplanning.controllers.dto.InterviewerSlotDTO;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidDayOfWeekException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidInterviewerException;
import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.period.PeriodService;
import com.intellias.intellistart.interviewplanning.model.user.Role;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.user.UserRepository;
import com.intellias.intellistart.interviewplanning.model.user.UserService;
import com.intellias.intellistart.interviewplanning.model.week.Week;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


/**
 * Service for InterviewSlot entity.
 */
@Service
public class InterviewerSlotService {

  private final InterviewerSlotRepository interviewerSlotRepository;
  private static  UserRepository userRepository;


  @Autowired
  public InterviewerSlotService(
      InterviewerSlotRepository interviewerSlotRepository, UserService userService, UserRepository userRepository) {
    this.interviewerSlotRepository = interviewerSlotRepository;
    InterviewerSlotService.userRepository = userRepository;
  }

  public static InterviewerSlot interviewerSlotValidation(InterviewerSlotDTO interviewerSlotDTO)
      throws InvalidDayOfWeekException, InvalidInterviewerException, InvalidBoundariesException {

    User user = userRepository.findById(interviewerSlotDTO.getInterviewerId()).get();
    if(!isInterviewerRoleINTERVIEWER(user))
      throw new InvalidInterviewerException();

    if (!isCorrectDay(interviewerSlotDTO.getDayOfWeek())) {
      throw new InvalidDayOfWeekException();
    }

    Period period = PeriodService.getPeriod(interviewerSlotDTO.getFrom(), interviewerSlotDTO.getTo()); // validate from to

    Week week = new Week(interviewerSlotDTO.getWeek(), new HashSet<>()); // getWeek
    DayOfWeek dayOfWeek = DayOfWeek.valueOf(interviewerSlotDTO.getDayOfWeek());
    User u1 =  new User(null, "interviewer@gmail.com", Role.INTERVIEWER, null);//userService.getCurrentUser();
    return new InterviewerSlot(null, week, dayOfWeek, period, new HashSet<>(), u1);
  }

//  public InterviewerSlot getCurrentUser(Long id) {
//
//
//  }

  public static boolean isInterviewerRoleINTERVIEWER(User user){
    return user.getRole().equals(Role.INTERVIEWER);
  }

  public static boolean isCorrectDay(String dayOfWeek){
    return ObjectUtils.containsConstant(DayOfWeek.values(), dayOfWeek);
  }


  public boolean canEditThisWeek(){
    return true;
  }

  public boolean isSlotOverlapping(){
    return false;
  }

}
