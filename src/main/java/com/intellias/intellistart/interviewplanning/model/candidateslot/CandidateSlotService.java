package com.intellias.intellistart.interviewplanning.model.candidateslot;

import com.intellias.intellistart.interviewplanning.exceptions.CandidateSlotNotFoundException;
import com.intellias.intellistart.interviewplanning.model.period.PeriodService;
import com.intellias.intellistart.interviewplanning.model.user.UserService;
import java.time.LocalDate;
import java.util.List;
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

  /**
   * Constructor.
   */
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
   *
   * @param candidateSlot - The object to be saved in the database.
   *
   * @return CandidateSlot - An object that was successfully saved in the database.
   */
  public CandidateSlot create(CandidateSlot candidateSlot) {
    return candidateSlotRepository.save(candidateSlot);
  }

  /**
   * Updated in DB the CandidateSlot object.
   *
   * @param candidateSlot - Updated slot data.
   * @param id - The id of the slot that we are going to update.
   *
   * @return CandidateSlot - An object that was successfully updated in the database.
   */
  public CandidateSlot update(CandidateSlot candidateSlot, Long id) {
    candidateSlot.setId(id);
    return create(candidateSlot);
  }


  /**
   * Returned slots of current Candidate.
   *
   * @return List of CandidateSlot - the list of slots of current candidate.
   */
  public List<CandidateSlot> getAllSlotsOfCandidate() {
    return candidateSlotRepository.findByUser(
        userService.getCurrentUser());
  }

  /**
   * Returned slots of current Candidate by date.
   *
   * @param date - date on which the database will be searched.
   *
   * @return List of CandidateSlot - Slots that were found in the database by given parameters.
   */
  public List<CandidateSlot> getCandidateSlotsByUserAndDate(LocalDate date) {
    return candidateSlotRepository.findByUserAndDate(
        userService.getCurrentUser(), date);
  }

  /**
   * Returned slot of current Candidate by id.
   *
   * @param id - The slot number to search for in the database.
   *
   * @return Optional of CandidateSlot - Optional object of find slot by id.
   */
  public CandidateSlot findById(Long id) {
    return candidateSlotRepository.findById(id).orElseThrow(CandidateSlotNotFoundException::new);
  }

  /**
   * Created CandidateSlot object by given parameters.
   *
   * @param date - slot date.
   * @param from - the time from which the slot will start.
   * @param to - the time by which the slot will end.
   *
   * @return CandidateSlot - created object by parameters.
   */
  public CandidateSlot createCandidateSlot(LocalDate date, String from, String to) {
    CandidateSlot candidateSlot = new CandidateSlot();

    candidateSlot.setDate(date);
    candidateSlot.setPeriod(periodService.obtainPeriod(from, to));
    candidateSlot.setUser(userService.getCurrentUser());

    return candidateSlot;
  }
}
