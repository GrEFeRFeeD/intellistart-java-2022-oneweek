package com.intellias.intellistart.interviewplanning.controllers;

import com.intellias.intellistart.interviewplanning.controllers.dto.CandidateSlotDto;
import com.intellias.intellistart.interviewplanning.controllers.dto.CandidateSlotsDto;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotIsBookedException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotIsOverlappingException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotNotFoundException;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlot;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlotService;
import com.intellias.intellistart.interviewplanning.model.candidateslot.validation.CandidateSlotValidator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for processing requests from Candidate.
 */
@RestController
public class CandidateController {

  private final CandidateSlotService candidateSlotService;
  private final CandidateSlotValidator candidateSlotValidator;

  @Autowired
  public CandidateController(CandidateSlotService candidateSlotService,
      CandidateSlotValidator candidateSlotValidator) {
    this.candidateSlotService = candidateSlotService;
    this.candidateSlotValidator = candidateSlotValidator;
  }

  /**
   * POST request for adding a new CandidateSlot.
   * First we do the conversion, then we pass it to the validation,
   * and then we send it to the service for saving.
   *
   * @param request - Request body of POST mapping.
   *
   * @return ResponseEntity - Response of the saved object converted to a DTO.
   *
   * @throws InvalidBoundariesException - when parameters are incorrect.
   * @throws SlotIsOverlappingException - when the slot is overlapping.
   */
  @PostMapping("/candidates/current/slots")
  public ResponseEntity<CandidateSlotDto> createCandidateSlot(@RequestBody CandidateSlotDto request)
      throws InvalidBoundariesException, SlotIsOverlappingException {
    CandidateSlot candidateSlot = getCandidateSlotFromDto(request);
    candidateSlotValidator.validateCreateCandidateSlot(candidateSlot);
    candidateSlot = candidateSlotService.create(candidateSlot);
    return ResponseEntity.ok(new CandidateSlotDto(candidateSlot));
  }

  /**
   * POST request for editing the CandidateSlot.
   * First we do the conversion, then we pass it to the validation,
   * and then we send it to the service for updating.
   *
   * @param request - Request body of POST mapping.
   * @param id - Parameter from the request mapping. This is the slot id for update.
   *
   * @return ResponseEntity - Response of the updated object converted to a DTO.
   *
   * @throws SlotNotFoundException - when updated slot id not found.
   * @throws SlotIsBookedException - when updated slot is booked.
   * @throws InvalidBoundariesException - when parameters are incorrect.
   * @throws SlotIsOverlappingException - when the slot is overlapping.
   */
  @PostMapping("/candidates/current/slots/{slotId}")
  public ResponseEntity<CandidateSlotDto> updateCandidateSlot(@RequestBody CandidateSlotDto request,
      @PathVariable("slotId") Long id)
      throws SlotNotFoundException, SlotIsBookedException, InvalidBoundariesException,
      SlotIsOverlappingException {
    CandidateSlot candidateSlot = getCandidateSlotFromDto(request);
    candidateSlot.setId(id);
    candidateSlotValidator.validateUpdateCandidateSlot(candidateSlot, id);
    candidateSlot = candidateSlotService.update(candidateSlot, id);
    return ResponseEntity.ok(new CandidateSlotDto(candidateSlot));
  }

  /**
   * GET request for getting all slots of current Candidate.
   *
   * @return ResponseEntity - Response of the list of slots converted to a DTO.
   */
  @GetMapping("/candidates/current/slots")
  public ResponseEntity<CandidateSlotsDto> getAllSlotsOfCandidate() {
    List<CandidateSlot> candidateSlots = candidateSlotService.getAllSlotsOfCandidate();
    return ResponseEntity.ok(getCandidateSlotsDtoFromListOf(candidateSlots));
  }

  /**
   * Converts the candidate slot from the DTO.
   *
   * @param candidateSlotDto - DTO of Candidate slot.
   *
   * @return CandidateSlot object by given DTO.
   */
  private CandidateSlot getCandidateSlotFromDto(CandidateSlotDto candidateSlotDto) {
    return candidateSlotService.createCandidateSlot(candidateSlotDto.getDate(),
        candidateSlotDto.getFrom(), candidateSlotDto.getTo());
  }

  /**
   * Created the list of CandidateSlotDto object by given list of CandidateSlot.
   *
   * @param candidateSlotList - List of candidates to convert.
   *
   * @return candidateSlotsDto - List of DTO by given list of CandidateSlot.
   */
  private CandidateSlotsDto getCandidateSlotsDtoFromListOf(List<CandidateSlot> candidateSlotList) {
    List<CandidateSlotDto> candidateSlotDtoList = candidateSlotList
        .stream()
        .map(CandidateSlotDto::new)
        .collect(Collectors.toList());
    CandidateSlotsDto candidateSlotsDto = new CandidateSlotsDto();
    candidateSlotsDto.setCandidateSlotDtoList(candidateSlotDtoList);
    return candidateSlotsDto;
  }
}
