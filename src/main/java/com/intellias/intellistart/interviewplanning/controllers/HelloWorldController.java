package com.intellias.intellistart.interviewplanning.controllers;

import com.intellias.intellistart.interviewplanning.model.user.UserRepository;
import com.intellias.intellistart.interviewplanning.security.JwtUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: REMOVE
/**
 * Test controller.
 */
// TODO: remove
@RestController
public class HelloWorldController {

  private final UserRepository userRepository;

  @Autowired
  public HelloWorldController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Say hello to world.
   */
  @RequestMapping({"/hello"})
  public String firstPage(Authentication authentication) {

    JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();

    return "CURRENT USER = " + jwtUserDetails.getName() + " AND " + jwtUserDetails.getEmail();
  }

  /**
   * Testing ROLE_TEST role.
   */
  @GetMapping("/test")
  public String getRoles() {

    return "Welcome, ROLE_TEST role owner!";
  }

  /**
   * Testing TEST authority.
   */
  @GetMapping("/testa")
  public String getRolesA() {

    return "Welcome, TEST authority owner!";
  }
}