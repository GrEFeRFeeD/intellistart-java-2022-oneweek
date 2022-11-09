package com.intellias.intellistart.interviewplanning.model.user;

import com.intellias.intellistart.interviewplanning.exceptions.SelfRevokingException;
import com.intellias.intellistart.interviewplanning.exceptions.UserAlreadyHasRoleException;
import com.intellias.intellistart.interviewplanning.exceptions.UserHasAnotherRoleException;
import com.intellias.intellistart.interviewplanning.exceptions.UserNotFoundException;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService;
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
  private final InterviewerSlotService interviewerSlotService;

  @Autowired
  public UserService(UserRepository userRepository,
      InterviewerSlotService interviewerSlotService) {
    this.userRepository = userRepository;
    this.interviewerSlotService = interviewerSlotService;
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
   * @throws UserAlreadyHasRoleException - when user already has role.
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

  /**
   * Method will return the interviewer whom we will delete.
   * Before deleting, the method checks if the submitted id is really the interviewer.
   * The method also deletes all the interviewer's bookings and slots before deleting.
   *
   * @param id - the interviewer's id to delete.
   *
   * @return User - the deleted user.
   *
   * @throws UserNotFoundException - when the user not found by given id.
   * @throws UserHasAnotherRoleException - when the user has not interviewer role;
   */
  public User deleteInterviewer(Long id) throws UserNotFoundException, UserHasAnotherRoleException {
    User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

    if (user.getRole() != Role.INTERVIEWER) {
      throw new UserHasAnotherRoleException();
    }

    interviewerSlotService.deleteSlotsByUser(user);

    userRepository.delete(user);
    return user;
  }

  /**
   * Method will return the coordinator whom we will delete.
   * Before deleting, the method checks that the coordinator to be deleted is not himself.
   *
   * @param id - the coordinator's id to delete.
   * @param currentEmailCoordinator - email of current user.
   *
   * @return User - the deleted user.
   *
   * @throws UserNotFoundException - when the user not found by given id.
   * @throws SelfRevokingException - when the coordinator removes himself.
   */
  public User deleteCoordinator(Long id, String currentEmailCoordinator)
      throws UserNotFoundException, SelfRevokingException {

    User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    User currentUser = userRepository.findByEmail(currentEmailCoordinator);

    if (user.getId() == currentUser.getId()) {
      throw new SelfRevokingException();
    }

    userRepository.delete(user);
    return user;
  }
}

