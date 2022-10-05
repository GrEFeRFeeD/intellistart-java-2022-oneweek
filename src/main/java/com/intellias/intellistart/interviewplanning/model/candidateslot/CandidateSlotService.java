package com.intellias.intellistart.interviewplanning.model.candidateslot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for CandidateSlot entity.
 */
@Service
public class CandidateSlotService {

  private final CandidateSlotRepository candidateSlotRepository;

  @Autowired
  public CandidateSlotService(CandidateSlotRepository candidateSlotRepository) {
    this.candidateSlotRepository = candidateSlotRepository;
  }
}
