package com.intellias.intellistart.interviewplanning.model.candidateslot;

import com.intellias.intellistart.interviewplanning.controllers.dto.CandidateSlotDto;
import com.intellias.intellistart.interviewplanning.model.period.PeriodService;
import com.intellias.intellistart.interviewplanning.model.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for CandidateSlot entity.
 */
@Service
public class CandidateSlotService {

  private final CandidateSlotRepository candidateSlotRepository;
  private final PeriodService periodService;
  private final UserService userService;

  @Autowired
  public CandidateSlotService(CandidateSlotRepository candidateSlotRepository,
      PeriodService periodService,
      UserService userService) {
    this.candidateSlotRepository = candidateSlotRepository;
    this.periodService = periodService;
    this.userService = userService;
  }

  /**
   * Created CandidateSlot objects with by given DTO.
   *
   * @return CandidateSlot object.
   */
  public CandidateSlot create(CandidateSlotDto candidateSlotsDto) {
    CandidateSlot candidateSlot = getCandidateSlotFromDto(candidateSlotsDto);
    return candidateSlotRepository.save(candidateSlot);
  }

  private CandidateSlot getCandidateSlotFromDto(CandidateSlotDto candidateSlotDto) {
    CandidateSlot candidateSlot = new CandidateSlot();
    candidateSlot.setDate(candidateSlotDto.getDate());
    candidateSlot.setPeriod(periodService.getPeriod(candidateSlotDto.getFrom(),
        candidateSlotDto.getTo()));
    candidateSlot.setUser(userService.getCurrentUser());
    return candidateSlot;
  }
}
