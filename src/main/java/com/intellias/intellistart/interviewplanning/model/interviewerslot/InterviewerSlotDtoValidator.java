package com.intellias.intellistart.interviewplanning.model.interviewerslot;


import com.intellias.intellistart.interviewplanning.controllers.dto.InterviewerSlotDto;
import com.intellias.intellistart.interviewplanning.exceptions.CannotEditThisWeekException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidDayOfWeekException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidInterviewerException;
import com.intellias.intellistart.interviewplanning.exceptions.SecurityException.SecurityExceptionProfile;
import com.intellias.intellistart.interviewplanning.exceptions.SlotIsNotFoundException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotIsOverlappingException;
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
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
   * @throws InvalidDayOfWeekException   - when invalid day of week
   * @throws InvalidInterviewerException - when invalid interviewer id, role not Interviewer
   * @throws SlotIsOverlappingException  - when overlap some slot
   * @throws InvalidBoundariesException  - when not in range 10:00 - 22:00, or less than 90 min

   */
  public void interviewerSlotValidateDtoAndCreate(InterviewerSlotDto interviewerSlotDto,
      Authentication authentication, Long userId)
      throws InvalidDayOfWeekException, InvalidInterviewerException, InvalidBoundariesException,
      SlotIsOverlappingException, CannotEditThisWeekException {

    validateIfCorrectDay(interviewerSlotDto.getDayOfWeek());

    Period period = periodService.obtainPeriod(interviewerSlotDto.getFrom(),
        interviewerSlotDto.getTo());
    Week week = weekService.getWeekByWeekNum(interviewerSlotDto.getWeek());
    validateIfCanEditThisWeek(week);

    DayOfWeek dayOfWeek = DayOfWeek.valueOf(interviewerSlotDto.getDayOfWeek());

    User user = validateAndGetUser(userId, authentication);
    interviewerSlotDto.setInterviewerId(userId);

    validateIfPeriodIsOverlapping(period, week, user, dayOfWeek);

    InterviewerSlot interviewerSlot = new InterviewerSlot(null, week,
        dayOfWeek, period, null, user);

    if (interviewerSlotDto.getInterviewerSlotId() != null) {
      interviewerSlot.setId(interviewerSlotDto.getInterviewerSlotId());
    }

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
   * @throws InvalidDayOfWeekException - when invalid day of week
   * @throws InvalidInterviewerException - when invalid interviewer id, role not Interviewer
   * @throws InvalidBoundariesException - when not in range 10:00 - 22:00, or less than 90 min
   * @throws SlotIsOverlappingException - when overlap some slot
   * @throws CannotEditThisWeekException - when editing week is current or next on SAT or SUN
   * @throws SlotIsNotFoundException - when slot is not found by slotId
   */
  public void interviewerSlotValidateDtoAndUpdate(InterviewerSlotDto interviewerSlotDto,
      Authentication authentication, Long userId, Long slotId)
      throws InvalidDayOfWeekException, InvalidInterviewerException, InvalidBoundariesException,
      SlotIsOverlappingException, CannotEditThisWeekException, SlotIsNotFoundException {

    Optional<InterviewerSlot> interviewerSlotOptional = interviewerSlotService.getSlotById(slotId);

    if (interviewerSlotOptional.isEmpty()) {
      throw new SlotIsNotFoundException();
    }

    Long ownerOfSlotId = interviewerSlotOptional.get().getUser().getId();

    if (!(ownerOfSlotId.equals(userId))) {
      throw new SecurityException(String.valueOf(SecurityExceptionProfile.ACCESS_DENIED));
    }

    interviewerSlotDto.setInterviewerSlotId(slotId);

    interviewerSlotValidateDtoAndCreate(interviewerSlotDto,
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
   * @throws InvalidInterviewerException - when user by id and by authentication is not the same
   */
  public User validateAndGetUser(Long userId, Authentication authentication)
      throws InvalidInterviewerException, SecurityException {
    JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
    String email = jwtUserDetails.getEmail();
    User userById = userService.getUserById(userId)
        .orElseThrow(InvalidInterviewerException::new);
    if (email.equals(userById.getEmail())) {
      validateIfInterviewerRoleInterviewer(userById);
      return userById;
    }
    //TODO resolve without valueOf
    throw new SecurityException(String.valueOf(SecurityExceptionProfile.ACCESS_DENIED));
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
   * Throw error if new week from InterviewerSlot is not in the future or if current week's day is
   * before this week's Friday 00:00. Get InterviewerSlot, then check if it's week is not in the
   * past or if it's week is current week Finally, check if dayOfWeek is not Saturday or Sunday.
   *
   * @param week from Controller's request
   * @throws CannotEditThisWeekException - CannotEditThisWeekException
   */
  public void validateIfCanEditThisWeek(Week week)
      throws CannotEditThisWeekException {

    Week currentWeek = weekService.getCurrentWeek();
    if (week.getId() <= currentWeek.getId()) {
      throw new CannotEditThisWeekException();
    }
    LocalDate currentDate = LocalDate.now();
    DayOfWeek currentDayOfWeek = weekService.getDayOfWeek(currentDate);
    if (week.getId() == currentWeek.getId() + 1) {
      if (currentDayOfWeek.equals(DayOfWeek.SAT)
          || currentDayOfWeek.equals(DayOfWeek.SUN)) {
        throw new CannotEditThisWeekException();
      }
    }
  }

  /**
   * Throw error if new Period is overlapping any other Period of this User - on this Week and this
   * DayOfWeek. Get List of InterviewerSlots from database where Week, User and DayOfWeek match
   * parameters. Then check every slot if it overlaps our new Period.
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
        if (periodService.areOverlapping(interviewerSlot.getPeriod(), period)) {
          throw new SlotIsOverlappingException();
        }
      }
    }
  }
}
