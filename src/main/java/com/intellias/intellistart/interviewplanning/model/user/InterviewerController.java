package com.intellias.intellistart.interviewplanning.model.user;

import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for interviewer endpoints.
 */
@RestController
public class InterviewerController {

  private InterviewerSlotService interviewerSlotService;

  @Autowired
  public InterviewerController(InterviewerSlotService interviewerSlotService) {
    this.interviewerSlotService = interviewerSlotService;
  }
}
