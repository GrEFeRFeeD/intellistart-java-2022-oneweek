package com.intellias.intellistart.interviewplanning.model.user;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for User entity.
 */
@Service
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
  public User getCurrentUser(){
    User user = userRepository.findById(1L).get();
    return user;
  }
  
  /**
   * Method for gaining Optional User by id.
   *
   * @return Optional User by id.
   */
  public Optional<User> getUserById(Long id) {
    return userRepository.findById(id);
  }
}

