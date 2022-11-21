package com.intellias.intellistart.interviewplanning.model.interviewerslot;

import com.intellias.intellistart.interviewplanning.controllers.dto.InterviewerSlotDto;
import com.intellias.intellistart.interviewplanning.exceptions.SecurityException;
import com.intellias.intellistart.interviewplanning.exceptions.SecurityException.SecurityExceptionProfile;
import com.intellias.intellistart.interviewplanning.exceptions.SlotException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotException.SlotExceptionProfile;
import com.intellias.intellistart.interviewplanning.exceptions.UserException;
import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.period.PeriodService;
import com.intellias.intellistart.interviewplanning.model.user.Role;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.user.UserService;
import com.intellias.intellistart.interviewplanning.model.week.Week;
import com.intellias.intellistart.interviewplanning.model.week.WeekService;
import com.intellias.intellistart.interviewplanning.security.JwtUserDetails;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * Service for validation of Interviewer Slot DTO.
 */
@Service
public class InterviewerSlotDtoValidator {

  private final PeriodService periodService;
  private final UserService userService;
  private final InterviewerSlotService interviewerSlotService;
  private final WeekService weekService;
  private final InterviewerSlotRepository interviewerSlotRepository;

  /**
   * Constructor.
   */
  @Autowired
  public InterviewerSlotDtoValidator(PeriodService periodService,
      UserService userService, InterviewerSlotService interviewerSlotService,
      WeekService weekService, InterviewerSlotRepository interviewerSlotRepository) {
    this.periodService = periodService;
    this.userService = userService;
    this.interviewerSlotService = interviewerSlotService;
    this.weekService = weekService;
    this.interviewerSlotRepository = interviewerSlotRepository;
  }

  /**
   * Validate interviewerSlotDTO for User, DayOfWeek, Period. If interviewerSlotDTO is correct:
   * save InterviewerSlot in database. If not - throws one of the exceptions.
   *
   * @param interviewerSlotDto from Controller's request
   *
   * @throws SlotException when:
   *     <ul>
   *     <li>invalid interviewer id, role not Interviewer
   *     <li>slots are overlapping
   *     <li>invalid boundaries of time period
   *     </ul>
   */
  public void validateAndCreate(InterviewerSlotDto interviewerSlotDto,
      Authentication authentication, Long userId)
      throws UserException, SlotException {

    validateIfCorrectDay(interviewerSlotDto.getDayOfWeek());

    Period period = periodService.obtainPeriod(interviewerSlotDto.getFrom(),
        interviewerSlotDto.getTo());
    Week week = weekService.getWeekByWeekNum(interviewerSlotDto.getWeek());
    validateIfCanEditThisWeek(week);

    DayOfWeek dayOfWeek = DayOfWeek.valueOf(interviewerSlotDto.getDayOfWeek());

    User user = validateAndGetUser(userId, authentication);
    interviewerSlotDto.setInterviewerId(userId);


    InterviewerSlot interviewerSlot = new InterviewerSlot(null, week,
        dayOfWeek, period, null, user);

    if (interviewerSlotDto.getInterviewerSlotId() != null) {
      interviewerSlot.setId(interviewerSlotDto.getInterviewerSlotId());
    }
    validateIfPeriodIsOverlapping(interviewerSlot);

    interviewerSlot.getWeek().addInterviewerSlot(interviewerSlot);
    interviewerSlot = interviewerSlotService.create(interviewerSlot);
    interviewerSlotDto.setInterviewerSlotId(interviewerSlot.getId());

  }

  /**
   * Get slotId from request, check if it exists and if it belongs to current user.
   * Go to interviewerSlotValidateDtoAndCreate.
   *
   * @param interviewerSlotDto - from request
   * @param authentication - from springSecurity
   * @param userId - from request
   * @param slotId - from request
   * @throws SlotException - when:
   *     <ul>
   *     <li>extreme values validation
   *     <li>invalid boundaries of time period
   *     <li>when slot is not found by slotId
   *     <li>slot is overlapping
   *     <li>invalid day of week
   *     </ul>
   *
   * @throws UserException - when invalid interviewer id, role not Interviewer
   * @throws SlotException - when slot has at least one or more bookings
   */
  public void validateAndUpdate(InterviewerSlotDto interviewerSlotDto,
      Authentication authentication, Long userId, Long slotId)
      throws UserException, SlotException {

    InterviewerSlot interviewerSlot = interviewerSlotService.findById(slotId);

    if (!(interviewerSlot.getUser().getId().equals(userId))) {
      throw new SecurityException(SecurityExceptionProfile.ACCESS_DENIED);
    }

    if (interviewerSlot.getBookings() != null) {
      throw new SlotException(SlotExceptionProfile.SLOT_IS_BOOKED);
    }

    interviewerSlotDto.setInterviewerSlotId(slotId);

    validateAndCreate(interviewerSlotDto,
        authentication, userId);
  }

  /**
   * Get userId and authentication from request. Compare user's from database email with
   * authentication's email. Return user if user is the same. Throw exception when different email
   * or user by id is not exist.
   *
   * @param userId         - id from request
   * @param authentication - authentication
   * @return User
   * @throws UserException - when user by id and by authentication is not the same
   */
  public User validateAndGetUser(Long userId, Authentication authentication)
      throws UserException, SecurityException {
    JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
    String email = jwtUserDetails.getEmail();
    User userById = userService.getUserById(userId)
        .orElseThrow(() ->
                new UserException(UserException.UserExceptionProfile.INVALID_INTERVIEWER));
    if (email.equals(userById.getEmail())) {
      validateIfInterviewerRoleInterviewer(userById);
      return userById;
    }

    throw new SecurityException(SecurityExceptionProfile.ACCESS_DENIED);
  }

  /**
   * Get User and check if User's role is INTERVIEWER.
   *
   * @param user Interviewer
   * @throws UserException - InvalidInterviewerException
   */
  public void validateIfInterviewerRoleInterviewer(User user) throws UserException {
    if (!user.getRole().equals(Role.INTERVIEWER)) {
      throw new UserException(UserException.UserExceptionProfile.INVALID_INTERVIEWER);
    }
  }

  /**
   * Get DayOfWeek in String and check if it is one of Enums DayOfWeek.
   *
   * @param dayOfWeek dayOfWeek from interviewerSlotDTO
   *
   * @throws SlotException invalid day of week
   */
  public void validateIfCorrectDay(String dayOfWeek) throws SlotException {
    if (!ObjectUtils.containsConstant(DayOfWeek.values(), dayOfWeek)) {
      throw new SlotException(SlotExceptionProfile.INVALID_DAY_OF_WEEK);
    }
  }

  /**
   * Throw error if new week from InterviewerSlot is not in the future or if current week's day is
   * before this week's Friday 00:00. Get InterviewerSlot, then check if it's week is not in the
   * past or if it's week is current week Finally, check if dayOfWeek is not Saturday or Sunday.
   *
   * @param week from Controller's request
   * @throws SlotException - CannotEditThisWeekException
   */
  public void validateIfCanEditThisWeek(Week week)
      throws SlotException {

    Week currentWeek = weekService.getCurrentWeek();
    if (week.getId() <= currentWeek.getId()) {
      throw new SlotException(SlotExceptionProfile.CANNOT_EDIT_THIS_WEEK);
    }
    LocalDate currentDate = LocalDate.now();
    DayOfWeek currentDayOfWeek = weekService.getDayOfWeek(currentDate);
    if (week.getId() == currentWeek.getId() + 1) {
      if (currentDayOfWeek.equals(DayOfWeek.SAT)
          || currentDayOfWeek.equals(DayOfWeek.SUN)) {
        throw new SlotException(SlotExceptionProfile.CANNOT_EDIT_THIS_WEEK);
      }
    }
  }

  /**
   * Throw error if new Period is overlapping any other Period of this User - on this Week and this
   * DayOfWeek. Get List of InterviewerSlots from database where Week, User and DayOfWeek match
   * parameters. Then check every slot if it overlaps our new Period.
   *
   * @param interviewerSlot - slot from dto
   * @throws SlotException - when given slot is overlapping another one
   */
  public void validateIfPeriodIsOverlapping(InterviewerSlot interviewerSlot)
      throws SlotException {

    List<InterviewerSlot> interviewerSlotsList = interviewerSlotRepository
        .getInterviewerSlotsByUserIdAndWeekIdAndDayOfWeek(interviewerSlot.getUser().getId(),
            interviewerSlot.getWeek().getId(), interviewerSlot.getDayOfWeek());

    if (!interviewerSlotsList.isEmpty()) {

      if (interviewerSlot.getId() != null) {
        interviewerSlotsList = interviewerSlotsList
            .stream()
            .filter(slot -> !slot.getId().equals(interviewerSlot.getId()))
            .collect(Collectors.toList());
      }

      for (InterviewerSlot temp : interviewerSlotsList) {
        if (periodService.areOverlapping(temp.getPeriod(), interviewerSlot.getPeriod())) {
          throw new SlotException(SlotExceptionProfile.SLOT_IS_OVERLAPPING);
        }
      }

    }
  }
}
