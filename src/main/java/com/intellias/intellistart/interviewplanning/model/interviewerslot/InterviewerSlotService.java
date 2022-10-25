package com.intellias.intellistart.interviewplanning.model.interviewerslot;

import static com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotDTOValidator.interviewerSlotValidateDTO;

import com.intellias.intellistart.interviewplanning.controllers.dto.InterviewerSlotDTO;
import com.intellias.intellistart.interviewplanning.exceptions.CannotEditThisWeekException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidDayOfWeekException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidInterviewerException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotIsOverlappingException;
import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.period.PeriodRepository;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.user.UserRepository;
import com.intellias.intellistart.interviewplanning.model.week.Week;
import com.intellias.intellistart.interviewplanning.model.week.WeekRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Service for InterviewSlot entity.
 */
@Service
public class InterviewerSlotService {

  private static  InterviewerSlotRepository interviewerSlotRepository;
  private static  UserRepository userRepository;
  private static PeriodRepository periodRepository;
  private static WeekRepository weekRepository;



  @Autowired
  public InterviewerSlotService(
      InterviewerSlotRepository interviewerSlotRepository, UserRepository userRepository,
      PeriodRepository periodRepository, WeekRepository weekRepository) {
    InterviewerSlotService.interviewerSlotRepository = interviewerSlotRepository;
    InterviewerSlotService.userRepository = userRepository;
    InterviewerSlotService.periodRepository = periodRepository;
    InterviewerSlotService.weekRepository = weekRepository;
  }

  /**
   * Get InterviewerSlotDTO from Request, validate it and returns InterviewerSlot
   * if all fields are correct, otherwise throws exception
   *
   * @param interviewerSlotDTO - from request
   * @return InterviewerSlot
   * @throws InvalidDayOfWeekException
   * @throws SlotIsOverlappingException
   * @throws InvalidBoundariesException
   * @throws InvalidInterviewerException
   */
  public static InterviewerSlot interviewerSlotValidation(InterviewerSlotDTO interviewerSlotDTO)
      throws InvalidDayOfWeekException, SlotIsOverlappingException,
      InvalidBoundariesException, InvalidInterviewerException, CannotEditThisWeekException {
          return interviewerSlotValidateDTO(interviewerSlotDTO);
  }

  public static Optional<InterviewerSlot> getSlotByIdOne(){
    return interviewerSlotRepository.findById(1L);
  }
  public static InterviewerSlot createInterviewerSlot(User user, Week week, DayOfWeek dayOfWeek, Period period){
    return interviewerSlotRepository.save(new InterviewerSlot(null, week, dayOfWeek, period, null, user));
  }
  public static Optional<Period> getPeriodById(Long id){
    return periodRepository.findById(id);
  }

  /**
   * Get user, week, dayOfWeek.
   * Than make a select in InterviewerSlotRepository
   * where user, week and dayOfWeek are the match with parameters
   * @param user - current user
   * @param week - week from DTO
   * @param dayOfWeek - day from DTO
   * @return List<InterviewerSlot>
   */
  public static List<InterviewerSlot> getInterviewerSlots(User user, Week week, DayOfWeek dayOfWeek){
    return interviewerSlotRepository.getInterviewerSlotsByUserAndWeekAndDayOfWeek(user, week, dayOfWeek);
  }

  public static Optional<InterviewerSlot> getSlotById(Long id){
    return interviewerSlotRepository.findById(id);
  }

}
