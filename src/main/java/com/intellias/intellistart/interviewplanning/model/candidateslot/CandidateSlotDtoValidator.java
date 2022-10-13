package com.intellias.intellistart.interviewplanning.model.candidateslot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidateSlotDtoValidator {
  private final CandidateSlotService candidateSlotService;

  @Autowired
  public CandidateSlotDtoValidator(CandidateSlotService candidateSlotService) {
    this.candidateSlotService = candidateSlotService;
  }


}
