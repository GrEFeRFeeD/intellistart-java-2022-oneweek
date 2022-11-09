package com.intellias.intellistart.interviewplanning.controllers;

import com.intellias.intellistart.interviewplanning.controllers.dto.EmailDto;
import com.intellias.intellistart.interviewplanning.controllers.dto.Users;
import com.intellias.intellistart.interviewplanning.exceptions.SelfRevokingException;
import com.intellias.intellistart.interviewplanning.exceptions.UserAlreadyHasRoleException;
import com.intellias.intellistart.interviewplanning.exceptions.UserHasAnotherRoleException;
import com.intellias.intellistart.interviewplanning.exceptions.UserNotFoundException;
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

  private final UserService userService;

  @Autowired
  public  CoordinatorController(UserService userService) {
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
  public ResponseEntity<Users> getAllInterviewers() {
    Users users = new Users();
    users.setUsers(userService.obtainUsersByRole(Role.INTERVIEWER));
    return ResponseEntity.ok(users);
  }

  /**
   * GET request to get a list of users with the coordinator role.
   *
   * @return ResponseEntity - Response of the list of users with the coordinator role.
   */
  @GetMapping("/users/coordinators")
  public ResponseEntity<Users> getAllCoordinators() {
    Users users = new Users();
    users.setUsers(userService.obtainUsersByRole(Role.COORDINATOR));
    return ResponseEntity.ok(users);
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
}
