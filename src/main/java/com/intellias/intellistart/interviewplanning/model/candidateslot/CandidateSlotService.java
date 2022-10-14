package com.intellias.intellistart.interviewplanning.model.candidateslot;

import com.intellias.intellistart.interviewplanning.controllers.dto.CandidateSlotDto;
import com.intellias.intellistart.interviewplanning.controllers.dto.CandidateSlotsDto;
import com.intellias.intellistart.interviewplanning.exeptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.exeptions.SlotIsBookedException;
import com.intellias.intellistart.interviewplanning.exeptions.SlotIsOverlappingException;
import com.intellias.intellistart.interviewplanning.exeptions.SlotNotFoundException;
import com.intellias.intellistart.interviewplanning.model.period.PeriodService;
import com.intellias.intellistart.interviewplanning.model.user.UserService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for CandidateSlot entity.
 */
@Service
public class CandidateSlotService {

  private final CandidateSlotRepository candidateSlotRepository;
  private final CandidateSlotDtoValidator candidateSlotDtoValidator;
  private final PeriodService periodService;
  private final UserService userService;

  @Autowired
  public CandidateSlotService(CandidateSlotRepository candidateSlotRepository,
      PeriodService periodService,
      UserService userService,
      CandidateSlotDtoValidator candidateSlotDtoValidator) {
    this.candidateSlotRepository = candidateSlotRepository;
    this.periodService = periodService;
    this.userService = userService;
    this.candidateSlotDtoValidator = candidateSlotDtoValidator;
  }

  /**
   * Created in DB the CandidateSlot object with by given DTO.
   * @param candidateSlotDto
   * @return
   * @throws InvalidBoundariesException
   * @throws SlotIsOverlappingException
   */
  public CandidateSlot create(CandidateSlotDto candidateSlotDto)
      throws InvalidBoundariesException, SlotIsOverlappingException {
    candidateSlotDtoValidator.validateCreateCandidateSlotDto(candidateSlotDto);
    CandidateSlot candidateSlot = getCandidateSlotFromDto(candidateSlotDto);
    return candidateSlotRepository.save(candidateSlot);
  }

  /**
   * Update CandidateSlot object with by given DTO.
   * @param candidateSlotDto
   * @param id
   * @return
   * @throws SlotNotFoundException
   * @throws SlotIsBookedException
   * @throws InvalidBoundariesException
   * @throws SlotIsOverlappingException
   */
  public CandidateSlot update(CandidateSlotDto candidateSlotDto, Long id)
      throws SlotNotFoundException, SlotIsBookedException, InvalidBoundariesException, SlotIsOverlappingException {
    candidateSlotDtoValidator.validateUpdateCandidateSlotDto(candidateSlotDto, id);
    CandidateSlot candidateSlot = getCandidateSlotFromDto(candidateSlotDto);
    candidateSlot.setId(id);
    return candidateSlotRepository.save(candidateSlot);
  }


  /**
   * Returned CandidateSlotsDto of current Candidate
   * @return candidateSlotsDto
   */
  public CandidateSlotsDto getAllSlotsOfCandidate() {
    List<CandidateSlot> candidateSlotList = candidateSlotRepository.getCandidateSlotsByUser(
        userService.getCurrentUser());
    return getCandidateSlotsDtoFromListOf(candidateSlotList);
  }

  /**
   * Created CandidateSlot object by given DTO.
   * @param candidateSlotDto
   * @return candidateSlot
   */
  private CandidateSlot getCandidateSlotFromDto(CandidateSlotDto candidateSlotDto) {
    CandidateSlot candidateSlot = new CandidateSlot();
    candidateSlot.setDate(candidateSlotDto.getDate());
    candidateSlot.setPeriod(periodService.getPeriod(candidateSlotDto.getFrom(),
        candidateSlotDto.getTo()));
    candidateSlot.setUser(userService.getCurrentUser());
    return candidateSlot;
  }

  /**
   * Created the list of CandidateSlotDto object by given list of CandidateSlot.
   * @param candidateSlotList
   * @return candidateSlotsDto
   */
  private CandidateSlotsDto getCandidateSlotsDtoFromListOf(List<CandidateSlot> candidateSlotList) {
    List<CandidateSlotDto> candidateSlotDtoList = candidateSlotList
        .stream()
        .map(this::getDtoFromCandidateSlot)
        .collect(Collectors.toList());
    CandidateSlotsDto candidateSlotsDto = new CandidateSlotsDto();
    candidateSlotsDto.setCandidateSlotDtoList(candidateSlotDtoList);
    return candidateSlotsDto;
  }


  /**
   * Created CandidateSlotDto object by given CandidateSlot.
   * @param item
   * @return candidateSlotDto
   */
  private CandidateSlotDto getDtoFromCandidateSlot(CandidateSlot item) {
    CandidateSlotDto candidateSlotDto = new CandidateSlotDto();
    candidateSlotDto.setDate(item.getDate());
    candidateSlotDto.setFrom(item.getPeriod().getFrom().toString());
    candidateSlotDto.setTo(item.getPeriod().getTo().toString());
    return candidateSlotDto;
  }
}
