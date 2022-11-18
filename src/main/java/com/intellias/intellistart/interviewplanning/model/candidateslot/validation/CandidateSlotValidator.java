package com.intellias.intellistart.interviewplanning.model.candidateslot.validation;

import com.intellias.intellistart.interviewplanning.exceptions.SlotException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotException.SlotExceptionProfile;
import com.intellias.intellistart.interviewplanning.exceptions.old.CandidateSlotNotFoundException;
import com.intellias.intellistart.interviewplanning.exceptions.old.SlotIsBookedException;
import com.intellias.intellistart.interviewplanning.exceptions.old.SlotIsOverlappingException;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlot;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlotService;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.period.PeriodService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Validator for CandidateSlot.
 */
@Service
public class CandidateSlotValidator {
  private final CandidateSlotService candidateSlotService;
  private final PeriodService periodService;

  @Autowired
  public CandidateSlotValidator(CandidateSlotService candidateSlotService,
      PeriodService periodService) {
    this.candidateSlotService = candidateSlotService;
    this.periodService = periodService;
  }

  /**
   * Validate CandidateSlot object for what the slot should be in the future,
   * whether the slot is not overlapping.
   *
   * @param candidateSlot - the slot that we will validate.
   *
   * @throws SlotException - when parameters are incorrect.
   * @throws SlotIsOverlappingException - when the slot is overlapping.
   */
  public void validateCreating(CandidateSlot candidateSlot)
      throws SlotException, SlotIsOverlappingException {
    validateSlotInFuture(candidateSlot);
    validateOverlapping(candidateSlot);
  }

  /**
   * Validate CandidateSlot object for all slot creation checks,
   * whether the slot exists, whether the slot is not booking.
   *
   * @param candidateSlot - the updated slot that we will validate.
   * @param id - the number of slot that we must update.
   *
   * @throws SlotException - when parameters are incorrect.
   * @throws CandidateSlotNotFoundException - when the slot not found in DB by given id.
   * @throws SlotIsBookedException - when updated slot is booked.
   * @throws SlotIsOverlappingException - when the slot is overlapping.
   */
  public void validateUpdating(CandidateSlot candidateSlot, Long id)
      throws SlotException, CandidateSlotNotFoundException, SlotIsBookedException,
      SlotIsOverlappingException {
    validateSlotIsBookingAndTheSlotExists(id);
    validateCreating(candidateSlot);
  }

  /**
   * Validate that date in CandidateSlot in the future.
   *
   * @param candidateSlot - the slot that we will validate.
   *
   * @throws SlotException - when parameters are incorrect.
   */
  private void validateSlotInFuture(CandidateSlot candidateSlot) throws SlotException {
    if (LocalDate.now().isAfter(candidateSlot.getDate())) {
      throw new SlotException(SlotExceptionProfile.INVALID_BOUNDARIES);
    }
  }

  /**
   * Validate that the slot does not overlap with other slots.
   *
   * @param candidateSlot - the slot that we validate.
   *
   * @throws SlotIsOverlappingException - when the slot is overlapping.
   */
  private void validateOverlapping(CandidateSlot candidateSlot) throws SlotIsOverlappingException {
    Period period = candidateSlot.getPeriod();

    List<CandidateSlot> candidateSlotList =
        candidateSlotService.getCandidateSlotsByEmailAndDate(candidateSlot.getEmail(),
            candidateSlot.getDate());

    if (!candidateSlotList.isEmpty()) {
      for (CandidateSlot item : candidateSlotList) {
        if (candidateSlot.getId() != null && candidateSlot.getId().equals(item.getId())) {
          continue;
        }
        if (periodService.areOverlapping(period, item.getPeriod())) {
          throw new SlotIsOverlappingException();
        }
      }
    }
  }

  /**
   * Validate for the given number of slot is exist in DB,
   * whether the slot is not booking.
   *
   * @param id - - the number of slot that we must check.
   *
   * @throws CandidateSlotNotFoundException - when id not found in DB.
   * @throws SlotIsBookedException - when slot is booked.
   */
  private void validateSlotIsBookingAndTheSlotExists(Long id)
      throws CandidateSlotNotFoundException, SlotIsBookedException {
    CandidateSlot candidateSlot = candidateSlotService.findById(id);

    if (!candidateSlot.getBookings().isEmpty()) {
      throw new SlotIsBookedException();
    }
  }
}
