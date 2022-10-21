package com.intellias.intellistart.interviewplanning.model.interviewerslot;

import static com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService.getInterviewerSlots;
import static com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService.getPeriodById;
import static com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService.getUserById;
import static com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService.getWeekById;

import com.intellias.intellistart.interviewplanning.controllers.dto.InterviewerSlotDTO;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidDayOfWeekException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidInterviewerException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotIsOverlappingException;
import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.user.Role;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.week.Week;
import com.intellias.intellistart.interviewplanning.model.week.WeekService;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.util.ObjectUtils;

@NonNull
public class InterviewerSlotDTOValidator {

  /**
   * Validate interviewerSlotDTO for User, DayOfWeek, Period.
   * If interviewerSlotDTO is correct returns InterviewerSlot.
   * If not - throws one of the exceptions.
   * @param interviewerSlotDTO from Controller's request
   *
   * @throws InvalidDayOfWeekException
   * @throws InvalidInterviewerException
   * @throws SlotIsOverlappingException
   * @throws InvalidBoundariesException
   *
   * return InterviewerSlot
   */
  public static InterviewerSlot interviewerSlotValidateDTO(InterviewerSlotDTO interviewerSlotDTO)
      throws InvalidDayOfWeekException, InvalidInterviewerException, InvalidBoundariesException,
      SlotIsOverlappingException {

    Optional<User> userOptional = getUserById(interviewerSlotDTO.getInterviewerId());
    User user;
      if (userOptional.isPresent()) {
        user = userOptional.get();
      } else
        throw new InvalidInterviewerException();

    if(!isInterviewerRoleINTERVIEWER(user))
      throw new InvalidInterviewerException();

    if (!isCorrectDay(interviewerSlotDTO.getDayOfWeek()))
      throw new InvalidDayOfWeekException();


    Period period = getPeriodById(1L).get();
    //PeriodService.getPeriod(interviewerSlotDTO.getFrom(), interviewerSlotDTO.getTo());


    Week week = WeekService.getWeekByWeekNum(interviewerSlotDTO.getWeek());

    DayOfWeek dayOfWeek = DayOfWeek.valueOf(interviewerSlotDTO.getDayOfWeek());

    if(isSlotOverlapping(period, week, user, dayOfWeek))
      throw new SlotIsOverlappingException();


    return new InterviewerSlot(null, week, dayOfWeek, period, null, user);
  }

  /**
   * Get User and check if User's role is INTERVIEWER.
   * @param user Interviewer
   * @return boolean
   */
  public static boolean isInterviewerRoleINTERVIEWER(User user){
    return user.getRole().equals(Role.INTERVIEWER);
  }

  /**
   * Get DayOfWeek in String and check if it is one of Enums DayOfWeek.
   * @param dayOfWeek dayOfWeek from interviewerSlotDTO
   * @return boolean
   */
  public static boolean isCorrectDay(String dayOfWeek){
    return ObjectUtils.containsConstant(DayOfWeek.values(), dayOfWeek);
  }

  /**
   * Returns true if week from InterviewerSlot is in the future and is not before this week's Friday 00:00.
   * Get InterviewerSlot, then check if it's week is not in the past or if it's week is current week
   * Finally, check if dayOfWeek is not Saturday or Sunday.
   * @param interviewerSlot from Controller's request
   * @return boolean
   */
  public static boolean canEditThisWeek(InterviewerSlot interviewerSlot){
    Week currentWeek = new Week(40L, new HashSet<>());//WeekService.getCurrentWeek();
    System.out.println(interviewerSlot.getWeek().getId() + "  iii " + currentWeek.getId());
    if(interviewerSlot.getWeek().getId() <= currentWeek.getId())
      return false;

    LocalDate currentDate = LocalDate.now();
    DayOfWeek currentDayOfWeek = WeekService.getDayOfWeek(currentDate);

    return !(currentDayOfWeek.equals(DayOfWeek.SAT) ||
          currentDayOfWeek.equals(DayOfWeek.SUN));
  }

  /**
   * Returns true if new Period is not overlapping any other Period
   * of this User on this Week and this DayOfWeek
   *
   * Get List of InterviewerSlots from database where Week, User and DayOfWeek match parameters.
   * Then check every slot if it overlaps our new Period
   *
   * @param period from Controller's request
   * @param week from Controller's request
   * @param user from Controller's request
   * @param dayOfWeek from Controller's request
   * @return boolean
   */
  public static boolean isSlotOverlapping(Period period,Week week, User user, DayOfWeek dayOfWeek){
    List<InterviewerSlot> allPeriods = getInterviewerSlots(user, week, dayOfWeek);

    for(InterviewerSlot interviewerSlot : allPeriods){
      System.out.println(interviewerSlot.getUser() + " " + interviewerSlot.getDayOfWeek() +
          " " + interviewerSlot.getWeek().getId());
//        if(PeriodService.isOverlap(tempPeriod, period)){
//          return true;
//        }

    }
    return false;
  }


}
