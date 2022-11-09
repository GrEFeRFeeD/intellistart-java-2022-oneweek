package com.intellias.intellistart.interviewplanning.model.user;

import com.intellias.intellistart.interviewplanning.exceptions.UserAlreadyHasRoleException;
import java.util.List;
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
  public User getCurrentUser() {
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

  /**
   * Returned the current user by given email.
   *
   * @param email - email on which the database will be searched.
   *
   * @return User - user object with current info.
   */
  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  /**
   * Method for grant the user a role by email.
   *
   * @param email - email address of the user to whom we will give the role.
   * @param roleOfUser - the role to grant the user.
   *
   * @return User - user to whom we granted the role.
   *
   * @throws UserAlreadyHasRoleException - - when user already has role.
   */
  public User grantRoleByEmail(String email, Role roleOfUser) throws UserAlreadyHasRoleException {
    User user = getUserByEmail(email);
    if (user != null) {
      throw new UserAlreadyHasRoleException();
    }

    user = new User();
    user.setEmail(email);
    user.setRole(roleOfUser);

    return userRepository.save(user);
  }

  /**
   * Method returned the list of users by given role from DB.
   *
   * @param role - role on which the database will be searched.
   *
   * @return List of users by given role.
   */
  public List<User> obtainUsersByRole(Role role) {
    return userRepository.findByRole(role);
  }
}

