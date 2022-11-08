package com.intellias.intellistart.interviewplanning.model.user;

import com.intellias.intellistart.interviewplanning.exceptions.UserAlreadyHasRoleException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

public class UserServiceTest {

  private static UserRepository userRepository;
  private static UserService cut;
  private static User user1;
  private static User user2;
  private static User user3;
  private static User user4;

  @BeforeAll
  static void initialize() {
    userRepository = Mockito.mock(UserRepository.class);
    cut = new UserService(userRepository);

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
}
