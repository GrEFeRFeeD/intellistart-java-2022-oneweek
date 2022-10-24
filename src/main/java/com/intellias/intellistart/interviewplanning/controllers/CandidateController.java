package com.intellias.intellistart.interviewplanning.controllers;

import com.intellias.intellistart.interviewplanning.controllers.dto.CandidateSlotDto;
import com.intellias.intellistart.interviewplanning.controllers.dto.CandidateSlotsDto;
import com.intellias.intellistart.interviewplanning.exeptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.exeptions.SlotIsBookedException;
import com.intellias.intellistart.interviewplanning.exeptions.SlotIsOverlappingException;
import com.intellias.intellistart.interviewplanning.exeptions.SlotNotFoundException;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlot;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlotService;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlotValidator;
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
   * POST request for adding a new CandidateSlot
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
   * POST request for editing the CandidateSlot
   */
  @PostMapping("/candidates/current/slots/{slotId}")
  public ResponseEntity<CandidateSlot> updateCandidateSlot(@RequestBody CandidateSlotDto request,
      @PathVariable("slotId") Long id)
      throws SlotNotFoundException, SlotIsBookedException, InvalidBoundariesException,
      SlotIsOverlappingException {
    CandidateSlot candidateSlot = getCandidateSlotFromDto(request);
    candidateSlotValidator.validateUpdateCandidateSlot(candidateSlot, id);
    candidateSlot = candidateSlotService.update(candidateSlot, id);
    return ResponseEntity.ok(candidateSlot);
  }

  /**
   * GET request for getting all slots of Candidate
   */
  @GetMapping("/candidates/current/slots")
  public ResponseEntity<CandidateSlotsDto> getAllSlotsOfCandidate() {
    List<CandidateSlot> candidateSlots = candidateSlotService.getAllSlotsOfCandidate();
    return ResponseEntity.ok(getCandidateSlotsDtoFromListOf(candidateSlots));
  }

  private CandidateSlot getCandidateSlotFromDto(CandidateSlotDto candidateSlotDto) {
    return candidateSlotService.createCandidateSlot(candidateSlotDto.getDate(),
        candidateSlotDto.getFrom(), candidateSlotDto.getTo());
  }

  /**
   * Created the list of CandidateSlotDto object by given list of CandidateSlot.o
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
