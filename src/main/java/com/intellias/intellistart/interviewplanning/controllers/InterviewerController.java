package com.intellias.intellistart.interviewplanning.controllers;

import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for processing requests from users with Interview role.
 */
@RestController
public class InterviewerController {

  private InterviewerSlotService interviewerSlotService;

  @Autowired
  public InterviewerController(InterviewerSlotService interviewerSlotService) {
    this.interviewerSlotService = interviewerSlotService;
  }
}
