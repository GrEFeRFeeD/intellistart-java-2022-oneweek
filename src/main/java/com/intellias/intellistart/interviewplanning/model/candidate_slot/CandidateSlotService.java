package com.intellias.intellistart.interviewplanning.model.candidate_slot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidateSlotService {

  private CandidateSlotRepository candidateSlotRepository;

  @Autowired
  public CandidateSlotService(CandidateSlotRepository candidateSlotRepository) {
    this.candidateSlotRepository = candidateSlotRepository;
  }
}
