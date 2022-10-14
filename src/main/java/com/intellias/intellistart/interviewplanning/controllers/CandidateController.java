package com.intellias.intellistart.interviewplanning.controllers;

import com.intellias.intellistart.interviewplanning.controllers.dto.CandidateSlotDto;
import com.intellias.intellistart.interviewplanning.controllers.dto.CandidateSlotsDto;
import com.intellias.intellistart.interviewplanning.exeptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.exeptions.SlotIsBookedException;
import com.intellias.intellistart.interviewplanning.exeptions.SlotIsOverlappingException;
import com.intellias.intellistart.interviewplanning.exeptions.SlotNotFoundException;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlot;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for processing requests from Candidate.
 */
@RestController
public class CandidateController {

  private final CandidateSlotService candidateSlotService;

  @Autowired
  public CandidateController(CandidateSlotService candidateSlotService) {
    this.candidateSlotService = candidateSlotService;
  }

  /**
   * POST request for adding a new CandidateSlot
   * @param request
   * @return candidateSlot
   * @throws InvalidBoundariesException
   * @throws SlotIsOverlappingException
   */
  @PostMapping("/candidates/current/slots")
  public ResponseEntity<CandidateSlot> createCandidateSlot(@RequestBody CandidateSlotDto request)
      throws InvalidBoundariesException, SlotIsOverlappingException {
    CandidateSlot candidateSlot = candidateSlotService.create(request);
    return ResponseEntity.ok(candidateSlot);
  }

  /**
   * POST request for editing the CandidateSlot
   * @param request
   * @param id
   * @return candidateSlot
   * @throws SlotNotFoundException
   * @throws SlotIsBookedException
   * @throws InvalidBoundariesException
   * @throws SlotIsOverlappingException
   */
  @PostMapping("/candidates/current/slots/{slotId}")
  public ResponseEntity<CandidateSlot> updateCandidateSlot(@RequestBody CandidateSlotDto request,
      @PathVariable("slotId") Long id)
      throws SlotNotFoundException, SlotIsBookedException, InvalidBoundariesException, SlotIsOverlappingException {
    CandidateSlot candidateSlot = candidateSlotService.update(request, id);
    return ResponseEntity.ok(candidateSlot);
  }

  /**
   * GET request for getting all slots of Candidate
   * @return candidateSlotsDto
   */
  @GetMapping("/candidates/current/slots")
  public ResponseEntity<CandidateSlotsDto> getAllSlotsOfCandidate() {
    CandidateSlotsDto candidateSlotsDto = candidateSlotService.getAllSlotsOfCandidate();
    return ResponseEntity.ok(candidateSlotsDto);
  }
}
