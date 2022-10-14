package com.intellias.intellistart.interviewplanning.model.candidateslot;

import com.intellias.intellistart.interviewplanning.controllers.dto.CandidateSlotDto;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidateSlotDtoValidator {
  private final UserService userService;
  private final CandidateSlotRepository candidateSlotRepository;
  private final PeriodService periodService;

  @Autowired
  public CandidateSlotDtoValidator(UserService userService,
      CandidateSlotRepository candidateSlotRepository,
      PeriodService periodService) {
    this.userService = userService;
    this.candidateSlotRepository = candidateSlotRepository;
    this.periodService = periodService;
  }

  /**
   * Validate CreateCandidateSlotDto object for what the slot should be in the future,
   * minimum duration of the slot is 1.5 hours, range must be rounded up to 30 minutes,
   * whether the slot is overlapping
   * @param candidateSlotDto
   * @throws InvalidBoundariesException
   * @throws SlotIsOverlappingException
   */
  public void validateCreateCandidateSlotDto(CandidateSlotDto candidateSlotDto)
      throws InvalidBoundariesException, SlotIsOverlappingException {
    Period period = periodService.getPeriod(candidateSlotDto.getFrom(), candidateSlotDto.getTo());
    List<CandidateSlot> candidateSlotList = candidateSlotRepository.getCandidateSlotsByUserAndDate(
        userService.getCurrentUser(), candidateSlotDto.getDate());
    if (!candidateSlotList.isEmpty()){
      for (CandidateSlot candidateSlot : candidateSlotList) {
        if (periodService.isOverlap(period, candidateSlot.getPeriod())) {
          throw new SlotIsOverlappingException();
        }
      }
    }
    if (LocalDate.now().isAfter(candidateSlotDto.getDate()) &&
        userService.getCurrentUser().getRole() != Role.CANDIDATE) {
      throw new InvalidBoundariesException();
    }
  }

  /**
   * Vlidate CreateCandidateSlotDto object for all slot creation checks,
   * whether the slot exists, whether the slot is reserved
   * @param candidateSlotDto
   * @param id
   * @throws InvalidBoundariesException
   * @throws SlotNotFoundException
   * @throws SlotIsBookedException
   * @throws SlotIsOverlappingException
   */
  public void validateUpdateCandidateSlotDto(CandidateSlotDto candidateSlotDto, Long id)
      throws InvalidBoundariesException, SlotNotFoundException, SlotIsBookedException, SlotIsOverlappingException {
    validateCreateCandidateSlotDto(candidateSlotDto);
    if (!candidateSlotRepository.existsById(id)) {
      throw new SlotNotFoundException();
    } else if (!candidateSlotRepository.findById(id).get().getBookings().isEmpty()) {
      throw new SlotIsBookedException();
    }
  }
}
