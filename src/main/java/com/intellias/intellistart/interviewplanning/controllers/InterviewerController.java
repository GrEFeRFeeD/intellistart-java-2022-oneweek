package com.intellias.intellistart.interviewplanning.controllers;


import static com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService.interviewerSlotValidation;

import com.intellias.intellistart.interviewplanning.controllers.dto.InterviewerSlotDTO;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidDayOfWeekException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidInterviewerException;
import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotRepository;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.user.Role;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.week.Week;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Optional;
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

  private final InterviewerSlotRepository interviewerSlotRepository;

  @Autowired
  public InterviewerController(
      InterviewerSlotRepository interviewerSlotRepository) {
    this.interviewerSlotRepository = interviewerSlotRepository;
  }
  //save, overlap, all valid, refactor service
  @PostMapping("/interviewers/{interviewerId}/slots")
  public ResponseEntity<InterviewerSlotDTO> createInterviewerSlot(
      @RequestBody InterviewerSlotDTO interviewerSlotDTO, @PathVariable("interviewerId") Long interviewerId)
      throws InvalidDayOfWeekException, InvalidBoundariesException, InvalidInterviewerException {
    interviewerSlotDTO.setInterviewerId(interviewerId);

    InterviewerSlot interviewerSlot = interviewerSlotValidation(interviewerSlotDTO);
    System.out.println(interviewerSlotDTO);
    System.out.println(interviewerSlot);
    //interviewerSlotRepository.save(interviewerSlot);
    return new ResponseEntity<>(interviewerSlotDTO, HttpStatus.OK);
  }

  @PostMapping("/interviewers/{interviewerId}/slots/{slotId}")
  public ResponseEntity<InterviewerSlotDTO> changeInterviewerSlot(
      @RequestBody InterviewerSlotDTO interviewerSlotDTO, @PathVariable("interviewerId") Long interviewerId,
      @PathVariable("SlotId") Long slotId)
      throws InvalidDayOfWeekException, InvalidBoundariesException, InvalidInterviewerException {
    Optional<InterviewerSlot> interviewerSlot = interviewerSlotRepository.findById(slotId);

    if(interviewerSlot.isPresent()) {
      InterviewerSlot _interviewerSlot = interviewerSlot.get();
      interviewerSlotDTO.setInterviewerId(interviewerId);

      InterviewerSlot interviewerSlotNew = interviewerSlotValidation(interviewerSlotDTO);
      interviewerSlotNew.setBookings(_interviewerSlot.getBookings());
      interviewerSlotNew.setDayOfWeek(_interviewerSlot.getDayOfWeek());
      interviewerSlotNew.setPeriod(_interviewerSlot.getPeriod());
      interviewerSlotNew.setWeek(_interviewerSlot.getWeek());
      interviewerSlotNew.setUser(_interviewerSlot.getUser());

      System.out.println(interviewerSlotDTO);
      System.out.println(interviewerSlot);
      interviewerSlotRepository.save(interviewerSlotNew);
      return new ResponseEntity<>(interviewerSlotDTO, HttpStatus.OK);
      } else
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
