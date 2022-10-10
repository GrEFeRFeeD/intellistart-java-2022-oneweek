package com.intellias.intellistart.interviewplanning.model.user;

import org.springframework.beans.factory.annotation.Autowired;

public class UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User getCurrentUser() {

    return new User(null, "testuser@example.com", null, null);
  }
}
