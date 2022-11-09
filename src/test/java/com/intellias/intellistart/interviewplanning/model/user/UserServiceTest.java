package com.intellias.intellistart.interviewplanning.model.user;

import com.intellias.intellistart.interviewplanning.exceptions.UserAlreadyHasRoleException;
import com.intellias.intellistart.interviewplanning.exceptions.UserHasAnotherRoleException;
import com.intellias.intellistart.interviewplanning.exceptions.UserNotFoundException;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

public class UserServiceTest {

  private static UserRepository userRepository;
  private static InterviewerSlotService interviewerSlotService;
  private static UserService cut;
  private static User user1;
  private static User user2;
  private static User user3;
  private static User user4;

  @BeforeAll
  static void initialize() {
    userRepository = Mockito.mock(UserRepository.class);
    interviewerSlotService = Mockito.mock(InterviewerSlotService.class);
    cut = new UserService(userRepository, interviewerSlotService);

    user1 = new User();
    user1.setId(1L);
    user1.setEmail("test1@mail.com");
    user1.setRole(Role.COORDINATOR);

    user2 = new User();
    user2.setId(2L);
    user2.setEmail("test2@mail.com");
    user2.setRole(Role.INTERVIEWER);

    user3 = new User();
    user3.setEmail("test3@mail.com");
    user3.setRole(Role.COORDINATOR);

    user4 = new User();
    user4.setEmail("test4@mail.com");
    user4.setRole(Role.INTERVIEWER);
  }

  static Arguments[] getUserByEmailTestArgs(){
    return new Arguments[]{
        Arguments.arguments("test1@mail.com", user1),
        Arguments.arguments("test2@mail.com", user2)
    };
  }
  @ParameterizedTest
  @MethodSource("getUserByEmailTestArgs")
  void getUserByEmailTest(String email, User expected) {
    Mockito.when(userRepository.findByEmail(email)).thenReturn(expected);

    User actual = cut.getUserByEmail(email);
    Assertions.assertEquals(expected, actual);
  }

  static Arguments[] grantRoleByEmailTestArgs(){
    return new Arguments[]{
        Arguments.arguments("test3@mail.com", Role.COORDINATOR, user3),
        Arguments.arguments("test4@mail.com", Role.INTERVIEWER, user4)
    };
  }
  @ParameterizedTest
  @MethodSource("grantRoleByEmailTestArgs")
  void grantRoleByEmailTest(String email, Role role, User expected)
      throws UserAlreadyHasRoleException {
    Mockito.when(userRepository.findByEmail(email)).thenReturn(null);
    Mockito.when(userRepository.save(expected)).thenReturn(expected);

    User actual = cut.grantRoleByEmail(email, role);
    Assertions.assertEquals(expected, actual);
  }

  static Arguments[] grantRoleByEmailTestExcArgs(){
    return new Arguments[]{
        Arguments.arguments("test3@mail.com", Role.COORDINATOR, user3),
        Arguments.arguments("test4@mail.com", Role.INTERVIEWER, user4)
    };
  }
  @ParameterizedTest
  @MethodSource("grantRoleByEmailTestExcArgs")
  void grantRoleByEmailTestException(String email, Role role, User user) {
    Mockito.when(userRepository.findByEmail(email)).thenReturn(user);

    Class<UserAlreadyHasRoleException> actual = UserAlreadyHasRoleException.class;
    Assertions.assertThrows(actual, () -> cut.grantRoleByEmail(email, role));
  }

  static Arguments[] obtainUsersByRoleTestArgs(){
    return new Arguments[]{
        Arguments.arguments(Role.COORDINATOR, List.of(user1)),
        Arguments.arguments(Role.INTERVIEWER, List.of(user2)),
        Arguments.arguments(Role.INTERVIEWER, null)
    };
  }
  @ParameterizedTest
  @MethodSource("obtainUsersByRoleTestArgs")
  void obtainUsersByRoleTest(Role role, List<User> expected) {
    Mockito.when(userRepository.findByRole(role)).thenReturn(expected);

    List<User> actual = cut.obtainUsersByRole(role);
    Assertions.assertEquals(expected, actual);
  }

  static Arguments[] deleteInterviewerTestTestArgs(){
    return new Arguments[]{
        Arguments.arguments(4L, user4),
        Arguments.arguments(2L, user2),
    };
  }
  @ParameterizedTest
  @MethodSource("deleteInterviewerTestTestArgs")
  void deleteInterviewerTest(Long id, User expected)
      throws UserNotFoundException, UserHasAnotherRoleException {
    Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(expected));
    Mockito.doNothing().when(interviewerSlotService).deleteSlotsByUser(expected);

    User actual = cut.deleteInterviewer(id);
    Assertions.assertEquals(expected, actual);
  }

  static Arguments[] deleteInterviewerTestUserNotFoundTestArgs(){
    return new Arguments[]{
        Arguments.arguments(4L),
        Arguments.arguments(2L),
    };
  }
  @ParameterizedTest
  @MethodSource("deleteInterviewerTestUserNotFoundTestArgs")
  void deleteInterviewerUserNotFoundTest(Long id) {
    Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());

    Class<UserNotFoundException> actual = UserNotFoundException.class;
    Assertions.assertThrows(actual, () -> cut.deleteInterviewer(id));
  }

  static Arguments[] deleteInterviewerUserAlreadyHasRoleTestTestArgs(){
    return new Arguments[]{
        Arguments.arguments(1L, user1),
        Arguments.arguments(3L, user3),
    };
  }
  @ParameterizedTest
  @MethodSource("deleteInterviewerUserAlreadyHasRoleTestTestArgs")
  void deleteInterviewerUserAlreadyHasRoleTest(Long id, User user) {
    Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));

    Class<UserHasAnotherRoleException> actual = UserHasAnotherRoleException.class;
    Assertions.assertThrows(actual, () -> cut.deleteInterviewer(id));
  }
}
