package com.intellias.intellistart.interviewplanning.controllers;

import com.intellias.intellistart.interviewplanning.controllers.dto.CandidateSlotDto;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlot;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
   */
  @PostMapping("/candidates/current/slots")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<CandidateSlot> createCandidateSlot(@RequestBody CandidateSlotDto request) {
    CandidateSlot candidateSlot = candidateSlotService.create(request);
    return ResponseEntity.ok(candidateSlot);
  }

}
