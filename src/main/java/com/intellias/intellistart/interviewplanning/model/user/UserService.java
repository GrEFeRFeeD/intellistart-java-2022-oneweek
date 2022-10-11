package com.intellias.intellistart.interviewplanning.model.user;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Service for User entity.
 */
public class UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Method for gaining current authorized User object (not implemented yet).
   *
   * @return currently just newUser() without relationships.
   */
  public User getCurrentUser() {

    return new User(null, "testuser@example.com", null);
  }
}
