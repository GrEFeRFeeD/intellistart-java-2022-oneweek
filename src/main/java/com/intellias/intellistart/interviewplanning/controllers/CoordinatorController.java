package com.intellias.intellistart.interviewplanning.controllers;


import com.intellias.intellistart.interviewplanning.controllers.dto.EmailDto;
import com.intellias.intellistart.interviewplanning.controllers.dto.Users;
import com.intellias.intellistart.interviewplanning.exceptions.UserAlreadyHasRoleException;
import com.intellias.intellistart.interviewplanning.model.user.Role;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

  @GetMapping("/users/interviewers")
  public ResponseEntity<Users> getAllInterviewers() {
    Users users = new Users();
    users.setUsers(userService.obtainUsersByRole(Role.INTERVIEWER));
    return ResponseEntity.ok(users);
  }
}
