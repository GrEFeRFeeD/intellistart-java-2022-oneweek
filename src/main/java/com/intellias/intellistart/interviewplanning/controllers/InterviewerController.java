package com.intellias.intellistart.interviewplanning.controllers;

import com.intellias.intellistart.interviewplanning.controllers.dto.InterviewerSlotDto;
import com.intellias.intellistart.interviewplanning.exceptions.CannotEditThisWeekException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidDayOfWeekException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidInterviewerException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotIsNotFoundException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotIsOverlappingException;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotDtoValidator;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService;
import com.intellias.intellistart.interviewplanning.model.week.WeekService;
import com.intellias.intellistart.interviewplanning.security.JwtUserDetails;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

  private final InterviewerSlotService interviewerSlotService;
  private final WeekService weekService;
  private final InterviewerSlotDtoValidator interviewerSlotDtoValidator;

  /**
   * Constructor.
   *
   * @param interviewerSlotService - interviewerSlotService
   * @param interviewerSlotDtoValidator - interviewerSlotDtoValidator
   * @param weekService - weekService
   */
  @Autowired
  public InterviewerController(
      InterviewerSlotService interviewerSlotService,
      InterviewerSlotDtoValidator interviewerSlotDtoValidator,
      WeekService weekService) {
    this.interviewerSlotService = interviewerSlotService;
    this.interviewerSlotDtoValidator = interviewerSlotDtoValidator;
    this.weekService = weekService;
  }

  /**
   * Post Request for creating slot.
   *
   * @param interviewerSlotDto - DTO from request
   * @param interviewerId - user Id from request
   * @return interviewerSlotDto - and/or HTTP status
   * @throws InvalidDayOfWeekException - invalid day of week
   * @throws InvalidInterviewerException - invalid user (interviewer) exception
   * @throws SlotIsOverlappingException - slot is overlapping exception
   * @throws InvalidBoundariesException - invalid boundaries exception
   * @throws CannotEditThisWeekException - can not edit this week
   */
  @PostMapping("/interviewers/{interviewerId}/slots")
  public ResponseEntity<InterviewerSlotDto> createInterviewerSlot(
      @RequestBody InterviewerSlotDto interviewerSlotDto,
      @PathVariable("interviewerId") Long interviewerId)
      throws InvalidDayOfWeekException, InvalidBoundariesException, InvalidInterviewerException,
      SlotIsOverlappingException, CannotEditThisWeekException {

    interviewerSlotDto.setInterviewerId(interviewerId);

    InterviewerSlot interviewerSlot = interviewerSlotDtoValidator
        .interviewerSlotValidateDto(interviewerSlotDto);

    interviewerSlot.getWeek().addInterviewerSlot(interviewerSlot);

    interviewerSlotService.create(interviewerSlot);

    interviewerSlotDto.setInterviewerSlotId(interviewerSlot.getId());


    return new ResponseEntity<>(interviewerSlotDto, HttpStatus.OK);
  }

  /**
   * Post Request for updating slot.
   *
   * @param interviewerSlotDto - DTO from request
   * @param interviewerId - user Id from request
   * @param slotId - slot Id from request
   * @return interviewerSlotDto - and/or HTTP status
   * @throws InvalidDayOfWeekException - invalid day of week
   * @throws InvalidInterviewerException - invalid user (interviewer) exception
   * @throws SlotIsOverlappingException - slot is overlapping exception
   * @throws InvalidBoundariesException - invalid boundaries exception
   * @throws CannotEditThisWeekException - can not edit this week
   */
  @PostMapping("/interviewers/{interviewerId}/slots/{slotId}")
  public ResponseEntity<InterviewerSlotDto> updateInterviewerSlot(
      @RequestBody InterviewerSlotDto interviewerSlotDto,
      @PathVariable("interviewerId") Long interviewerId,
      @PathVariable("slotId") Long slotId)
      throws InvalidDayOfWeekException, InvalidBoundariesException,
      InvalidInterviewerException, SlotIsOverlappingException,
      CannotEditThisWeekException, SlotIsNotFoundException {

    Optional<InterviewerSlot> interviewerSlotOptional = interviewerSlotService.getSlotById(slotId);

    if (interviewerSlotOptional.isEmpty()) {
      throw new SlotIsNotFoundException();
    }
    Long id = interviewerSlotOptional.get().getId();

    interviewerSlotDto.setInterviewerId(interviewerId);

    InterviewerSlot interviewerSlotNew = interviewerSlotDtoValidator
        .interviewerSlotValidateDto(interviewerSlotDto);
    interviewerSlotNew.setId(id);

    interviewerSlotService.create(interviewerSlotNew);

    interviewerSlotDto.setInterviewerSlotId(interviewerSlotNew.getId());

    interviewerSlotNew.getWeek().addInterviewerSlot(interviewerSlotNew);

    return new ResponseEntity<>(interviewerSlotDto, HttpStatus.OK);
  }

  /**
   * Request for getting Interviewer Slots of current user
   * for current week.
   *
   * @param authentication - user
   * @return {@link List} of {@link InterviewerSlot}
   */
  @GetMapping("/interviwers/current/slots")
  public ResponseEntity<List<InterviewerSlot>> getInterviewerSlotsForCurrentWeek(
      Authentication authentication) {
    JwtUserDetails jwtUserDetails  = (JwtUserDetails) authentication.getPrincipal();

    String email = jwtUserDetails.getEmail();
    Long currentWeekId = weekService.getCurrentWeek().getId();

    List<InterviewerSlot> slots = interviewerSlotService.getSlotsByWeek(email, currentWeekId);

    return new ResponseEntity<>(slots, HttpStatus.OK);
  }

  /**
   * Request for getting Interviewer Slots of current user
   * for next week.
   *
   * @param authentication - user
   * @return {@link List} of {@link InterviewerSlot}
   */
  @GetMapping("/interviwers/next/slots")
  public ResponseEntity<List<InterviewerSlot>> getInterviewerSlotsForNextWeek(
      Authentication authentication) {
    JwtUserDetails jwtUserDetails  = (JwtUserDetails) authentication.getPrincipal();

    String email = jwtUserDetails.getEmail();
    Long nextWeekId = weekService.getNextWeek().getId();

    List<InterviewerSlot> slots = interviewerSlotService.getSlotsByWeek(email, nextWeekId);

    return new ResponseEntity<>(slots, HttpStatus.OK);
  }
}