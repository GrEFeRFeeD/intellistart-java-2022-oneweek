package com.intellias.intellistart.interviewplanning.model.user;

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
}
