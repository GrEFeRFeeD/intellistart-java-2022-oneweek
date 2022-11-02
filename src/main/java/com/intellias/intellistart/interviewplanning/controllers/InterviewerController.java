package com.intellias.intellistart.interviewplanning.controllers;

import com.intellias.intellistart.interviewplanning.controllers.dto.BookingLimitDto;
import com.intellias.intellistart.interviewplanning.controllers.dto.InterviewerSlotDto;
import com.intellias.intellistart.interviewplanning.exceptions.CannotEditThisWeekException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidDayOfWeekException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidInterviewerException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotIsNotFoundException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotIsOverlappingException;
import com.intellias.intellistart.interviewplanning.model.bookinglimit.BookingLimit;
import com.intellias.intellistart.interviewplanning.model.bookinglimit.BookingLimitService;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotDtoValidator;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService;
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

  private final InterviewerSlotService interviewerSlotService;
  private final InterviewerSlotDtoValidator interviewerSlotDtoValidator;
  private final BookingLimitService bookingLimitService;

  /**
   * Constructor.
   *
   * @param interviewerSlotService      - interviewerSlotService
   * @param interviewerSlotDtoValidator - interviewerSlotDtoValidator
   * @param bookingLimitService         - bookingLimitService
   */
  @Autowired
  public InterviewerController(
      InterviewerSlotService interviewerSlotService,
      InterviewerSlotDtoValidator interviewerSlotDtoValidator,
      BookingLimitService bookingLimitService) {
    this.interviewerSlotService = interviewerSlotService;
    this.interviewerSlotDtoValidator = interviewerSlotDtoValidator;
    this.bookingLimitService = bookingLimitService;
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
   * Post Request for creating booking limit.
   *
   * @param bookingLimitDto - DTO for BookingLimit
   * @param interviewerId   - user id from request
   * @return BookingLimitDto and HTTP status
   * @throws InvalidInterviewerException - invalid user (interviewer) exception
   */
  @PostMapping("/interviewers/{interviewerId}/bookinglimit")
  public ResponseEntity<BookingLimitDto> createBookingLimit(
      @RequestBody BookingLimitDto bookingLimitDto,
      @PathVariable("interviewerId") Long interviewerId)
      throws InvalidInterviewerException {

    bookingLimitDto.setUserId(interviewerId);

    BookingLimit bookingLimit = getBookingLimitFromDto(bookingLimitDto);

    return ResponseEntity.ok(new BookingLimitDto(bookingLimit));
  }

  private BookingLimit getBookingLimitFromDto(BookingLimitDto bookingLimitDto)
      throws InvalidInterviewerException {
    return bookingLimitService.createBookingLimit(bookingLimitDto.getUserId(),
        bookingLimitDto.getBookingLimit());
  }
}
