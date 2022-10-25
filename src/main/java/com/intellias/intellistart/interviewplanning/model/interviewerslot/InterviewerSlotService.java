package com.intellias.intellistart.interviewplanning.model.interviewerslot;

import com.intellias.intellistart.interviewplanning.controllers.dtos.InterviewerSlotDto;
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
  private final InterviewerSlotDtoValidator interviewerSlotDtoValidator;

  /**
   * Constructor for InterviewerSlotService
   * @param interviewerSlotDtoValidator - interviewerSlotDtoValidator
   * @param interviewerSlotRepository - interviewerSlotRepository
   * @param periodRepository - periodRepository
   */
  @Autowired
  public InterviewerSlotService(
      InterviewerSlotDtoValidator interviewerSlotDtoValidator,
      InterviewerSlotRepository interviewerSlotRepository,
      PeriodRepository periodRepository) {
    this.interviewerSlotDtoValidator = interviewerSlotDtoValidator;
    this.periodRepository = periodRepository;
    this.interviewerSlotRepository = interviewerSlotRepository;
  }

  /**
   * Get InterviewerSlotDto from Request, validate it and returns InterviewerSlot if all fields are
   * correct, otherwise throws exception.
   *
   * @param interviewerSlotDto - from request
   * @return InterviewerSlot
   * @throws InvalidDayOfWeekException - invalid day of week
   * @throws InvalidInterviewerException - invalid user (interviewer) exception
   * @throws SlotIsOverlappingException - slot is overlapping exception
   * @throws InvalidBoundariesException - invalid boundaries exception
   */
  public InterviewerSlot interviewerSlotValidation(InterviewerSlotDto interviewerSlotDto)
      throws InvalidDayOfWeekException, SlotIsOverlappingException,
      InvalidBoundariesException, InvalidInterviewerException, CannotEditThisWeekException {
    return interviewerSlotDtoValidator.interviewerSlotValidateDto(interviewerSlotDto);
  }

  /**
   * Just for tests, for getting first slot from StartDataLoad.
   *
   * @return Optional of Interviewer Slot
   */
  public Optional<InterviewerSlot> getSlotByIdOne() {
    return interviewerSlotRepository.findById(1L);
  }

  /**
   * Get all parameters for creating new Slot in database.
   *
   * @param user - current user
   * @param week - week
   * @param dayOfWeek - day of week
   * @param period - period
   * @return InterviewerSlot
   */
  public InterviewerSlot createInterviewerSlot(User user, Week week, DayOfWeek dayOfWeek,
      Period period) {
    return interviewerSlotRepository.save(
        new InterviewerSlot(null, week, dayOfWeek, period, null, user));
  }

  /**
   * Get Optional of period from database.
   *
   * @param id - Long id of Period to find
   *
   * @return {@link Optional} of {@link Period}
   */
  public Optional<Period> getPeriodById(Long id) {
    return periodRepository.findById(id);
  }

  /**
   * Get Optional of InterviewerSlot from database.
   *
   * @param id - Long id of InterviewerSlot to find
   *
   * @return {@link Optional} of {@link InterviewerSlot}
   */
  public Optional<InterviewerSlot> getSlotById(Long id) {
    return interviewerSlotRepository.findById(id);
  }
}
