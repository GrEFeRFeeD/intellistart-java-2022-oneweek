package com.intellias.intellistart.interviewplanning.model.interviewer_slot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterviewerSlotService {

  private InterviewerSlotRepository interviewerSlotRepository;

  @Autowired
  public InterviewerSlotService(InterviewerSlotRepository interviewerSlotRepository) {
    this.interviewerSlotRepository = interviewerSlotRepository;
  }
}
