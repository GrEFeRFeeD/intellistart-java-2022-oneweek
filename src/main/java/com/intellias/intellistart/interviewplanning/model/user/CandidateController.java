package com.intellias.intellistart.interviewplanning.model.user;

import com.intellias.intellistart.interviewplanning.model.candidate_slot.CandidateSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CandidateController {

  private CandidateSlotService candidateSlotService;

  @Autowired
  public CandidateController(CandidateSlotService candidateSlotService) {
    this.candidateSlotService = candidateSlotService;
  }
}
