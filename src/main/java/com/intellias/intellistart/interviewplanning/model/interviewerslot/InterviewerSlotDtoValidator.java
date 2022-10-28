package com.intellias.intellistart.interviewplanning.model.interviewerslot;


import com.intellias.intellistart.interviewplanning.controllers.dtos.InterviewerSlotDto;
import com.intellias.intellistart.interviewplanning.exceptions.CannotEditThisWeekException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidDayOfWeekException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidInterviewerException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotIsOverlappingException;
import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.period.PeriodService;
import com.intellias.intellistart.interviewplanning.model.user.Role;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.user.UserService;
import com.intellias.intellistart.interviewplanning.model.week.Week;
import com.intellias.intellistart.interviewplanning.model.week.WeekService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * Service for validation of Interviewer Slot DTO.
 */
@Component
@Service
public class InterviewerSlotDtoValidator {

  private final PeriodService periodService;
  private final UserService userService;
  private final WeekService weekService;
  private final InterviewerSlotRepository interviewerSlotRepository;

  /**
   * Constructor.
   */
  @Autowired
  public InterviewerSlotDtoValidator(PeriodService periodService,
      UserService userService, WeekService weekService,
      InterviewerSlotRepository interviewerSlotRepository) {
    this.periodService = periodService;
    this.userService = userService;
    this.weekService = weekService;
    this.interviewerSlotRepository = interviewerSlotRepository;
  }

  /**
   * Validate interviewerSlotDTO for User, DayOfWeek, Period. If interviewerSlotDTO is correct:
   * returns InterviewerSlot. If not - throws one of the exceptions.
   *
   * @param interviewerSlotDto from Controller's request
   * @return InterviewerSlot
   * @throws InvalidDayOfWeekException - invalid day of week
   * @throws InvalidInterviewerException - invalid user (interviewer) exception
   * @throws SlotIsOverlappingException - slot is overlapping exception
   * @throws InvalidBoundariesException - invalid boundaries exception
   */
  public InterviewerSlot interviewerSlotValidateDto(InterviewerSlotDto interviewerSlotDto)
      throws InvalidDayOfWeekException, InvalidInterviewerException, InvalidBoundariesException,
      SlotIsOverlappingException, CannotEditThisWeekException {

    User user = userService.getUserById(interviewerSlotDto.getInterviewerId())
        .orElseThrow(InvalidInterviewerException::new);

    validateIfInterviewerRoleInterviewer(user);

    validateIfCorrectDay(interviewerSlotDto.getDayOfWeek());

    Period period = periodService.getPeriod(interviewerSlotDto.getFrom(),
        interviewerSlotDto.getTo());
    Week week = weekService.getWeekByWeekNum(interviewerSlotDto.getWeek());
    DayOfWeek dayOfWeek = DayOfWeek.valueOf(interviewerSlotDto.getDayOfWeek());
    validateIfPeriodIsOverlapping(period, week, user, dayOfWeek);

    InterviewerSlot interviewerSlot = new InterviewerSlot(null, week,
        dayOfWeek, period, null, user);

    validateIfCanEditThisWeek(interviewerSlot);
    return interviewerSlot;
  }

  /**
   * Get User and check if User's role is INTERVIEWER.
   *
   * @param user Interviewer
   * @throws InvalidInterviewerException - InvalidInterviewerException
   */
  public void validateIfInterviewerRoleInterviewer(User user) throws InvalidInterviewerException {
    if (!user.getRole().equals(Role.INTERVIEWER)) {
      throw new InvalidInterviewerException();
    }
  }

  /**
   * Get DayOfWeek in String and check if it is one of Enums DayOfWeek.
   *
   * @param dayOfWeek dayOfWeek from interviewerSlotDTO
   * @throws InvalidDayOfWeekException - InvalidDayOfWeekException
   */
  public void validateIfCorrectDay(String dayOfWeek) throws InvalidDayOfWeekException {
    if (!ObjectUtils.containsConstant(DayOfWeek.values(), dayOfWeek)) {
      throw new InvalidDayOfWeekException();
    }
  }

  /**
   * Throw error if new week from InterviewerSlot is not in the future or if current week's day
   * is before this week's Friday 00:00.
   * Get InterviewerSlot, then check if it's week is not in the past or if it's week is
   * current week Finally, check if dayOfWeek is not Saturday or Sunday.
   *
   * @param interviewerSlot from Controller's request
   * @throws CannotEditThisWeekException - CannotEditThisWeekException
   */
  public void validateIfCanEditThisWeek(InterviewerSlot interviewerSlot)
      throws CannotEditThisWeekException {

    Week currentWeek = weekService.getCurrentWeek();
    if (interviewerSlot.getWeek().getId() <= currentWeek.getId()) {
      throw new CannotEditThisWeekException();
    }
    LocalDate currentDate = LocalDate.now();
    DayOfWeek currentDayOfWeek = weekService.getDayOfWeek(currentDate);
    if (interviewerSlot.getWeek().getId() == currentWeek.getId() + 1) {
      if (currentDayOfWeek.equals(DayOfWeek.SAT)
          || currentDayOfWeek.equals(DayOfWeek.SUN)) {
        throw new CannotEditThisWeekException();
      }
    }
  }

  /**
   * Throw error if new Period is overlapping any other Period of this User -
   * on this Week and this DayOfWeek.
   * Get List of InterviewerSlots from database where Week, User and DayOfWeek match parameters.
   * Then check every slot if it overlaps our new Period.
   *
   * @param period    from Controller's request
   * @param week      from Controller's request
   * @param user      from Controller's request
   * @param dayOfWeek from Controller's request
   * @throws SlotIsOverlappingException - SlotIsOverlappingException
   */
  public void validateIfPeriodIsOverlapping(Period period, Week week,
      User user, DayOfWeek dayOfWeek)
      throws SlotIsOverlappingException {
    List<InterviewerSlot> interviewerSlotsList = interviewerSlotRepository
        .getInterviewerSlotsByUserIdAndWeekIdAndDayOfWeek(user.getId(), week.getId(), dayOfWeek);
    if (!interviewerSlotsList.isEmpty()) {
      for (InterviewerSlot interviewerSlot : interviewerSlotsList) {
        if (periodService.isOverlap(interviewerSlot.getPeriod(), period)) {
          throw new SlotIsOverlappingException();
        }
      }
    }
  }
}