package com.intellias.intellistart.interviewplanning.controllers;


import static com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService.getSlotById;
import static com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService.interviewerSlotValidation;
import static com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService.validateBeforeUpdate;


import com.intellias.intellistart.interviewplanning.controllers.dto.InterviewerSlotDTO;
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
import org.springframework.web.bind.annotation.GetMapping;
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

  @PostMapping("/interviewers/{interviewerId}/slots")
  public ResponseEntity<InterviewerSlotDTO> createInterviewerSlot(
      @RequestBody InterviewerSlotDTO interviewerSlotDTO, @PathVariable("interviewerId") Long interviewerId)
      throws InvalidDayOfWeekException, InvalidBoundariesException, InvalidInterviewerException, SlotIsOverlappingException {
    interviewerSlotDTO.setInterviewerId(interviewerId);


    InterviewerSlot interviewerSlot = interviewerSlotValidation(interviewerSlotDTO);
    System.out.println(interviewerSlotDTO);
    interviewerSlot.getWeek().addInterviewerSlot(interviewerSlot);
    interviewerSlotRepository.save(interviewerSlot);


    return new ResponseEntity<>(interviewerSlotDTO, HttpStatus.OK);
  }

  @PostMapping("/interviewers/{interviewerId}/slots/{slotId}")
  public ResponseEntity<InterviewerSlotDTO> changeInterviewerSlot(
      @RequestBody InterviewerSlotDTO interviewerSlotDTO, @PathVariable("interviewerId") Long interviewerId,
      @PathVariable("slotId") Long slotId)
      throws InvalidDayOfWeekException, InvalidBoundariesException,
      InvalidInterviewerException, SlotIsOverlappingException, CannotEditThisWeekException, SlotIsNotFoundException {

      Optional<InterviewerSlot> interviewerSlotOptional = getSlotById(slotId);

      if(interviewerSlotOptional.isPresent()) {
        Long id = interviewerSlotOptional.get().getId();
        interviewerSlotDTO.setInterviewerId(interviewerId);

        InterviewerSlot interviewerSlotNew = interviewerSlotValidation(interviewerSlotDTO);
        interviewerSlotNew.setId(id);

        validateBeforeUpdate(interviewerSlotNew);

        System.out.println(interviewerSlotDTO);

        interviewerSlotRepository.save(interviewerSlotNew);

        return new ResponseEntity<>(interviewerSlotDTO, HttpStatus.OK);
      }
        throw new SlotIsNotFoundException();
        //return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

  @GetMapping("/interviewers/{interviewerId}/slots/{slotId}")
  public void getSlot(@PathVariable("interviewerId") Long interviewerId,
      @PathVariable("slotId") Long slotId) {
    Optional<InterviewerSlot> interviewerSlotOptional = getSlotById(slotId);
    interviewerSlotOptional.ifPresent(System.out::println);
  }
}
