package com.intellias.intellistart.interviewplanning.model.interviewerslot;

import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import com.intellias.intellistart.interviewplanning.model.period.Period;
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

  /**
   * Constructor for InterviewerSlotService.
   *
   * @param interviewerSlotRepository - interviewerSlotRepository
   */
  @Autowired
  public InterviewerSlotService(
      InterviewerSlotRepository interviewerSlotRepository) {
    this.interviewerSlotRepository = interviewerSlotRepository;
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

  /**
   * Get InterviewerSlot and save it in the DB.
   *
   * @param interviewerSlot - interviewerSlot
   * @return InterviewerSlot
   */
  public InterviewerSlot create(InterviewerSlot interviewerSlot) {
    return interviewerSlotRepository.save(interviewerSlot);
  }
}
