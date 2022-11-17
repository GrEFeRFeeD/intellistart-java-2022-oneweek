package com.intellias.intellistart.interviewplanning.controllers;

import com.intellias.intellistart.interviewplanning.controllers.dto.BookingDto;
import com.intellias.intellistart.interviewplanning.controllers.dto.EmailDto;
import com.intellias.intellistart.interviewplanning.controllers.dto.UsersDto;
import com.intellias.intellistart.interviewplanning.exceptions.CandidateSlotNotFoundException;
import com.intellias.intellistart.interviewplanning.exceptions.InterviewerSlotNotFoundException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.exceptions.NotInterviewerException;
import com.intellias.intellistart.interviewplanning.exceptions.SelfRevokingException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotIsNotFoundException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotsAreNotIntersectingException;
import com.intellias.intellistart.interviewplanning.exceptions.UserAlreadyHasRoleException;
import com.intellias.intellistart.interviewplanning.exceptions.UserHasAnotherRoleException;
import com.intellias.intellistart.interviewplanning.exceptions.UserNotFoundException;
import com.intellias.intellistart.interviewplanning.model.booking.Booking;
import com.intellias.intellistart.interviewplanning.model.booking.BookingService;
import com.intellias.intellistart.interviewplanning.model.booking.validation.BookingValidator;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlotService;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService;
import com.intellias.intellistart.interviewplanning.model.period.PeriodService;
import com.intellias.intellistart.interviewplanning.model.user.Role;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.user.UserService;
import com.intellias.intellistart.interviewplanning.security.JwtUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for processing requests from users with Coordinator role.
 */
@RestController
public class CoordinatorController {

  private final BookingService bookingService;
  private final BookingValidator bookingValidator;
  private final InterviewerSlotService interviewerSlotService;
  private final CandidateSlotService candidateSlotService;
  private final PeriodService periodService;
  private final UserService userService;

  /**
   * Constructor.
   */
  @Autowired
  public CoordinatorController(BookingService bookingService, BookingValidator bookingValidator,
      InterviewerSlotService interviewerSlotService, CandidateSlotService candidateSlotService,
      PeriodService periodService, UserService userService) {
    this.bookingService = bookingService;
    this.bookingValidator = bookingValidator;
    this.interviewerSlotService = interviewerSlotService;
    this.candidateSlotService = candidateSlotService;
    this.periodService = periodService;
    this.userService = userService;
  }

  /**
   * POST request to grant a INTERVIEWER role by email.
   *
   * @param request - Request body of POST mapping.
   *
   * @return ResponseEntity - Response of the granted User.
   *
   * @throws UserAlreadyHasRoleException - when user already has role.
   */
  @PostMapping("/users/interviewers")
  public ResponseEntity<User> grantInterviewerByEmail(@RequestBody EmailDto request)
      throws UserAlreadyHasRoleException {
    return ResponseEntity.ok(userService.grantRoleByEmail(request.getEmail(), Role.INTERVIEWER));
  }

  /**
   * POST request to grant a COORDINATOR role by email.
   *
   * @param request - Request body of POST mapping.
   *
   * @return ResponseEntity - Response of the granted User.
   *
   * @throws UserAlreadyHasRoleException - - when user already has role.
   */
  @PostMapping("/users/coordinators")
  public ResponseEntity<User> grantCoordinatorByEmail(@RequestBody EmailDto request)
      throws UserAlreadyHasRoleException {
    return ResponseEntity.ok(userService.grantRoleByEmail(request.getEmail(), Role.COORDINATOR));
  }

  /**
   * GET request to get a list of users with the interviewer role.
   *
   * @return ResponseEntity - Response of the list of users with the interviewer role.
   */
  @GetMapping("/users/interviewers")
  public ResponseEntity<UsersDto> getAllInterviewers() {
    UsersDto usersDto = new UsersDto();
    usersDto.setUsers(userService.obtainUsersByRole(Role.INTERVIEWER));
    return ResponseEntity.ok(usersDto);
  }

  /**
   * GET request to get a list of users with the coordinator role.
   *
   * @return ResponseEntity - Response of the list of users with the coordinator role.
   */
  @GetMapping("/users/coordinators")
  public ResponseEntity<UsersDto> getAllCoordinators() {
    UsersDto usersDto = new UsersDto();
    usersDto.setUsers(userService.obtainUsersByRole(Role.COORDINATOR));
    return ResponseEntity.ok(usersDto);
  }

  /**
   * DELETE request for deleting interviewer.
   *
   * @param id - the interviewer's id to delete.
   *
   * @return ResponseEntity - the deleted user.
   *
   * @throws UserNotFoundException - when the user not found by given id.
   * @throws UserHasAnotherRoleException - when the user has not interviewer role;
   */
  @DeleteMapping("/users/interviewers/{id}")
  public ResponseEntity<User> deleteInterviewerById(@PathVariable("id") Long id)
      throws UserNotFoundException, UserHasAnotherRoleException {
    return ResponseEntity.ok(userService.deleteInterviewer(id));
  }

  /**
   * DELETE request for deleting interviewer.
   *
   * @param id - the interviewer's id to delete.
   *
   * @return ResponseEntity - the deleted user.
   *
   * @throws UserNotFoundException - when the user not found by given id.
   * @throws SelfRevokingException - when the coordinator removes himself.
   */
  @DeleteMapping("/users/coordinators/{id}")
  public ResponseEntity<User> deleteCoordinatorById(@PathVariable("id") Long id,
      Authentication authentication)
      throws UserNotFoundException, SelfRevokingException, UserHasAnotherRoleException {

    JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
    String currentEmailCoordinator = jwtUserDetails.getEmail();
    return ResponseEntity.ok(userService.deleteCoordinator(id, currentEmailCoordinator));
  }

  /**
   * POST request method for updating booking by id.
   *
   * @param bookingDto request DTO
   *
   * @return ResponseEntity - Response of the saved updated object converted to a DTO.
   *
   * @throws InvalidBoundariesException if Period boundaries are Invalid
   * @throws CandidateSlotNotFoundException if CandidateSlot is not found
   * @throws InterviewerSlotNotFoundException if InterviewerSlot is not found
   * @throws SlotsAreNotIntersectingException if CandidateSlot, InterviewerSlot
   *     do not intersect with Period
   */
  @PostMapping("bookings/{id}")
  public ResponseEntity<BookingDto> updateBooking(
      @RequestBody BookingDto bookingDto,
      @PathVariable Long id) throws SlotIsNotFoundException, NotInterviewerException {

    Booking updatingBooking = bookingService.findById(id);
    Booking newDataBooking = getFromDto(bookingDto);

    bookingValidator.validateUpdating(updatingBooking, newDataBooking);
    populateFields(updatingBooking, newDataBooking);

    Booking savedBooking = bookingService.save(updatingBooking);
    return ResponseEntity.ok(new BookingDto(savedBooking));
  }

  /**
   * POST request method for adding booking.
   *
   * @param bookingDto request DTO
   *
   * @return ResponseEntity - Response of the saved created object converted to a DTO.
   *
   * @throws InvalidBoundariesException if Period boundaries are Invalid
   * @throws CandidateSlotNotFoundException if CandidateSlot is not found
   * @throws InterviewerSlotNotFoundException if InterviewerSlot is not found
   * @throws SlotsAreNotIntersectingException if CandidateSlot, InterviewerSlot
   *     do not intersect with Period
   */
  @PostMapping("bookings")
  public ResponseEntity<BookingDto> createBooking(
      @RequestBody BookingDto bookingDto) throws SlotIsNotFoundException, NotInterviewerException {

    Booking newBooking = getFromDto(bookingDto);

    bookingValidator.validateCreating(newBooking);
    Booking savedBooking = bookingService.save(newBooking);

    return ResponseEntity.ok(new BookingDto(savedBooking));
  }

  Booking getFromDto(BookingDto bookingDto) throws SlotIsNotFoundException {
    Booking booking = new Booking();

    booking.setSubject(bookingDto.getSubject());
    booking.setDescription(bookingDto.getDescription());

    booking.setInterviewerSlot(interviewerSlotService
        .findById(bookingDto.getInterviewerSlotId()));

    booking.setCandidateSlot(candidateSlotService
        .findById(bookingDto.getCandidateSlotId()));

    booking.setPeriod(periodService.obtainPeriod(
        bookingDto.getFrom(),
        bookingDto.getTo()));

    return booking;
  }

  private void populateFields(Booking booking, Booking newDataBooking) {
    booking.setSubject(newDataBooking.getSubject());
    booking.setDescription(newDataBooking.getDescription());
    booking.setInterviewerSlot(newDataBooking.getInterviewerSlot());
    booking.setCandidateSlot(newDataBooking.getCandidateSlot());
    booking.setPeriod(newDataBooking.getPeriod());
  }
}
