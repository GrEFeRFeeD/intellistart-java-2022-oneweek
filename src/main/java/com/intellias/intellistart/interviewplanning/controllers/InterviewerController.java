package com.intellias.intellistart.interviewplanning.controllers;


import com.intellias.intellistart.interviewplanning.controllers.dtos.InterviewerSlotDto;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService;

import com.intellias.intellistart.interviewplanning.exceptions.CannotEditThisWeekException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidDayOfWeekException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidInterviewerException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotIsNotFoundException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotIsOverlappingException;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotRepository;

import java.util.Optional;
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
  private final InterviewerSlotService interviewerSlotService;

  @Autowired
  public InterviewerController(
      InterviewerSlotRepository interviewerSlotRepository,
      InterviewerSlotService interviewerSlotService) {
    this.interviewerSlotRepository = interviewerSlotRepository;
    this.interviewerSlotService = interviewerSlotService;
  }

  @PostMapping("/interviewers/{interviewerId}/slots")
  public ResponseEntity<InterviewerSlotDto> createInterviewerSlot(
      @RequestBody InterviewerSlotDto interviewerSlotDTO,
      @PathVariable("interviewerId") Long interviewerId)
      throws InvalidDayOfWeekException, InvalidBoundariesException, InvalidInterviewerException, SlotIsOverlappingException, CannotEditThisWeekException {
    interviewerSlotDTO.setInterviewerId(interviewerId);

    InterviewerSlot interviewerSlot = interviewerSlotService.interviewerSlotValidation(
        interviewerSlotDTO);

    interviewerSlot.getWeek().addInterviewerSlot(interviewerSlot);
    interviewerSlotRepository.save(interviewerSlot);

    return new ResponseEntity<>(interviewerSlotDTO, HttpStatus.OK);
  }

  @PostMapping("/interviewers/{interviewerId}/slots/{slotId}")
  public ResponseEntity<InterviewerSlotDto> changeInterviewerSlot(
      @RequestBody InterviewerSlotDto interviewerSlotDTO,
      @PathVariable("interviewerId") Long interviewerId,
      @PathVariable("slotId") Long slotId)
      throws InvalidDayOfWeekException, InvalidBoundariesException,
      InvalidInterviewerException, SlotIsOverlappingException, CannotEditThisWeekException, SlotIsNotFoundException {

    Optional<InterviewerSlot> interviewerSlotOptional = interviewerSlotService.getSlotById(slotId);

    if (interviewerSlotOptional.isPresent()) {
      Long id = interviewerSlotOptional.get().getId();

      interviewerSlotDTO.setInterviewerId(interviewerId);

      InterviewerSlot interviewerSlotNew = interviewerSlotService.interviewerSlotValidation(
          interviewerSlotDTO);
      interviewerSlotNew.setId(id);

      interviewerSlotRepository.save(interviewerSlotNew);
      interviewerSlotNew.getWeek().addInterviewerSlot(interviewerSlotNew);

      return new ResponseEntity<>(interviewerSlotDTO, HttpStatus.OK);
    }
    throw new SlotIsNotFoundException();
    //return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }
}
