package com.intellias.intellistart.interviewplanning.model.candidateslot;

import com.intellias.intellistart.interviewplanning.exeptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.exeptions.SlotIsBookedException;
import com.intellias.intellistart.interviewplanning.exeptions.SlotIsOverlappingException;
import com.intellias.intellistart.interviewplanning.exeptions.SlotNotFoundException;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.period.PeriodService;
import com.intellias.intellistart.interviewplanning.model.user.Role;
import com.intellias.intellistart.interviewplanning.model.user.UserService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidateSlotValidator {
  private final CandidateSlotService candidateSlotService;
  private final PeriodService periodService;

  @Autowired
  public CandidateSlotValidator(UserService userService,
      CandidateSlotService candidateSlotService,
      PeriodService periodService) {
    this.candidateSlotService = candidateSlotService;
    this.periodService = periodService;
  }

  /**
   * Validate CandidateSlot object for what the slot should be in the future,
   * that the slot was created by the candidate,
   * whether the slot is not overlapping
   * @param candidateSlot
   * @throws InvalidBoundariesException
   * @throws SlotIsOverlappingException
   */
  public void validateCreateCandidateSlot(CandidateSlot candidateSlot)
      throws InvalidBoundariesException, SlotIsOverlappingException {
    validateSlotInFuture(candidateSlot);
    validateUserOnCandidate(candidateSlot);
    validateOverlapping(candidateSlot);
  }

  /**
   * Validate CandidateSlot object for all slot creation checks,
   * whether the slot exists, whether the slot is not booking
   * @param candidateSlot
   * @param id
   * @throws InvalidBoundariesException
   * @throws SlotNotFoundException
   * @throws SlotIsBookedException
   * @throws SlotIsOverlappingException
   */
  public void validateUpdateCandidateSlot(CandidateSlot candidateSlot, Long id)
      throws InvalidBoundariesException, SlotNotFoundException, SlotIsBookedException, SlotIsOverlappingException {
    validateSlotIsBookingAndTheSlotExists(id);
    validateCreateCandidateSlot(candidateSlot);
  }

  private void validateSlotInFuture(CandidateSlot candidateSlot) throws InvalidBoundariesException {
    if (LocalDate.now().isAfter(candidateSlot.getDate())) {
      throw new InvalidBoundariesException();
    }
  }

  private void validateUserOnCandidate(CandidateSlot candidateSlot)
      throws InvalidBoundariesException {
    if (candidateSlot.getUser().getRole()!=Role.CANDIDATE) {
      throw new InvalidBoundariesException();
    }
  }

  private void validateOverlapping(CandidateSlot candidateSlot) throws SlotIsOverlappingException {
    Period period = candidateSlot.getPeriod();
    List<CandidateSlot> candidateSlotList =
        candidateSlotService.getCandidateSlotsByUserAndDate(candidateSlot);
    if (!candidateSlotList.isEmpty()){
      for (CandidateSlot item : candidateSlotList) {
        if (periodService.isOverlap(period, item.getPeriod())) {
          throw new SlotIsOverlappingException();
        }
      }
    }
  }

  private void validateSlotIsBookingAndTheSlotExists(Long id)
      throws SlotNotFoundException, SlotIsBookedException {
    Optional<CandidateSlot> candidateSlotOptional = candidateSlotService.getCandidateSlotById(id);
    CandidateSlot candidateSlot;
    if (candidateSlotOptional.isPresent()) {
      candidateSlot = candidateSlotOptional.get();
    } else {
      throw new SlotNotFoundException();
    }
    if (!candidateSlot.getBookings().isEmpty()) {
      throw new SlotIsBookedException();
    }
  }
}
