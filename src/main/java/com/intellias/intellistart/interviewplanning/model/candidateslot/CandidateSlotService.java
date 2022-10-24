package com.intellias.intellistart.interviewplanning.model.candidateslot;

import com.intellias.intellistart.interviewplanning.model.period.PeriodService;
import com.intellias.intellistart.interviewplanning.model.user.UserService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
   * Created in DB the CandidateSlot object.
   */
  public CandidateSlot create(CandidateSlot candidateSlot) {
    return candidateSlotRepository.save(candidateSlot);
  }

  /**
   * Update CandidateSlot object with by given DTO.
   */
  public CandidateSlot update(CandidateSlot candidateSlot, Long id) {
    candidateSlot.setId(id);
    return candidateSlotRepository.save(candidateSlot);
  }


  /**
   * Returned CandidateSlots of current Candidate
   */
  public List<CandidateSlot> getAllSlotsOfCandidate() {
    return candidateSlotRepository.getCandidateSlotsByUser(
        userService.getCurrentUser());
  }

  public List<CandidateSlot> getCandidateSlotsByUserAndDate(CandidateSlot candidateSlot) {
    return candidateSlotRepository.getCandidateSlotsByUserAndDate(
        userService.getCurrentUser(), candidateSlot.getDate());
  }

  public Optional<CandidateSlot> getCandidateSlotById(Long id) {
    return candidateSlotRepository.findById(id);
  }

  /**
   * Created CandidateSlot object by given parameters
   */
  public CandidateSlot createCandidateSlot(LocalDate date, String from, String to) {
    CandidateSlot candidateSlot = new CandidateSlot();
    candidateSlot.setDate(date);
    candidateSlot.setPeriod(periodService.getPeriod(from, to));
    candidateSlot.setUser(userService.getCurrentUser());
    return candidateSlot;
  }
}
