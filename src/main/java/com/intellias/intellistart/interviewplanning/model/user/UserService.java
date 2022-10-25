package com.intellias.intellistart.interviewplanning.model.user;

import java.util.Optional;
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

    User user = userRepository.findById(1L).get();
    return user;
  }

  public Optional<User> getUserById(Long id){
    return userRepository.findById(id);
  }

}