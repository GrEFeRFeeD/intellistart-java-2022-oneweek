package com.intellias.intellistart.interviewplanning.controllers;


import static com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService.interviewerSlotValidation;

import com.intellias.intellistart.interviewplanning.controllers.dto.InterviewerSlotDTO;
import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.user.Role;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.week.Week;
import java.time.LocalTime;
import java.util.HashSet;
import javax.persistence.criteria.CriteriaBuilder.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for processing requests from users with Interview role.
 */
@RestController
public class InterviewerController {

  @Autowired
  public InterviewerController(InterviewerSlotService interviewerSlotService) {
  }

  @PostMapping("/interviewers/{interviewerId}/slots")
  public ResponseEntity<InterviewerSlot> createInterviewerSlot(
      @RequestBody InterviewerSlotDTO interviewerSlotDTO, @PathVariable("interviewerId") Long interviewerId)
      throws Exception {
    interviewerSlotDTO.setInterviewerId(interviewerId);

    InterviewerSlot interviewerSlot = interviewerSlotValidation(interviewerSlotDTO);
    System.out.println(interviewerSlotDTO);
    System.out.println(interviewerSlot);
    return new ResponseEntity<>(interviewerSlot, HttpStatus.OK);
  }

}
