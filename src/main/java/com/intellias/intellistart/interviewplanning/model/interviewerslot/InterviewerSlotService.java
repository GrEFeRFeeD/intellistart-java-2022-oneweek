package com.intellias.intellistart.interviewplanning.model.interviewerslot;

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
import com.intellias.intellistart.interviewplanning.model.week.Week;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Service for InterviewSlot entity.
 */
@Service
public class InterviewerSlotService {

  private final InterviewerSlotRepository interviewerSlotRepository;
  private final PeriodRepository periodRepository;
  private final  InterviewerSlotDTOValidator interviewerSlotDTOValidator;



  @Autowired
  public InterviewerSlotService(
      InterviewerSlotDTOValidator interviewerSlotDTOValidator,
      InterviewerSlotRepository interviewerSlotRepository,
      PeriodRepository periodRepository) {
    this.interviewerSlotDTOValidator = interviewerSlotDTOValidator;
    this.periodRepository = periodRepository;
    this.interviewerSlotRepository = interviewerSlotRepository;
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
  public InterviewerSlot interviewerSlotValidation(InterviewerSlotDTO interviewerSlotDTO)
      throws InvalidDayOfWeekException, SlotIsOverlappingException,
      InvalidBoundariesException, InvalidInterviewerException, CannotEditThisWeekException {
          return interviewerSlotDTOValidator.interviewerSlotValidateDTO(interviewerSlotDTO);
  }

  public  Optional<InterviewerSlot> getSlotByIdOne(){
    return interviewerSlotRepository.findById(1L);
  }
  public  InterviewerSlot createInterviewerSlot(User user, Week week, DayOfWeek dayOfWeek, Period period){
    return interviewerSlotRepository.save(new InterviewerSlot(null, week, dayOfWeek, period, null, user));
  }
  public  Optional<Period> getPeriodById(Long id){
    return periodRepository.findById(id);
  }



  public Optional<InterviewerSlot> getSlotById(Long id){
    return interviewerSlotRepository.findById(id);
  }

}
