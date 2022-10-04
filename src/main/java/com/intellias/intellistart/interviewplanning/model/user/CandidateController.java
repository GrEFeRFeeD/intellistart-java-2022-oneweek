package com.intellias.intellistart.interviewplanning.model.user;

import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for candidate endpoints.
 */
@RestController
public class CandidateController {

  private CandidateSlotService candidateSlotService;

  @Autowired
  public CandidateController(CandidateSlotService candidateSlotService) {
    this.candidateSlotService = candidateSlotService;
  }
}
