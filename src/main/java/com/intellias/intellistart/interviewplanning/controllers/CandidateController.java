package com.intellias.intellistart.interviewplanning.controllers;

import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for processing requests from users without role.
 */
@RestController
public class CandidateController {

  private final CandidateSlotService candidateSlotService;

  @Autowired
  public CandidateController(CandidateSlotService candidateSlotService) {
    this.candidateSlotService = candidateSlotService;
  }
}
