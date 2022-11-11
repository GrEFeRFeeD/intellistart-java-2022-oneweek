package com.intellias.intellistart.interviewplanning.model.bookinglimit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.intellias.intellistart.interviewplanning.exceptions.InvalidBookingLimitException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidInterviewerException;
import com.intellias.intellistart.interviewplanning.exceptions.NotInterviewerException;
import com.intellias.intellistart.interviewplanning.model.user.Role;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.user.UserService;
import com.intellias.intellistart.interviewplanning.model.week.Week;
import com.intellias.intellistart.interviewplanning.model.week.WeekService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

class BookingLimitServiceTest {

  private static BookingLimitRepository bookingLimitRepository;
  private static WeekService weekService;
  private static UserService userService;
  private static BookingLimitService cut;
  private static Week week1;
  private static Week week2;
  private static Week week3;
  private static User user1;
  private static User user2;
  private static User user3;
  private static User user4;
  private static BookingLimit bookingLimit1;
  private static BookingLimit bookingLimit2;
  private static BookingLimit bookingLimit3;
  private static BookingLimit bookingLimit4;

  @BeforeEach
  void initialize(){
    bookingLimitRepository = Mockito.mock(BookingLimitRepository.class);
    weekService = Mockito.mock(WeekService.class);
    userService = Mockito.mock(UserService.class);
    cut = new BookingLimitService(bookingLimitRepository, weekService, userService);
  }

  @BeforeAll
  static void initialize2() {

    user1 = new User();
    user1.setId(1L);
    user1.setRole(Role.INTERVIEWER);

    user2 = new User();
    user2.setId(2L);
    user2.setRole(Role.INTERVIEWER);

    user3 = new User();
    user3.setId(3L);
    user3.setRole(Role.COORDINATOR);

    user4 = new User();
    user4.setId(4L);
    user4.setRole(Role.COORDINATOR);

    week1 = new Week(40L,null);
    week2 = new Week(20L,null);
    week3 = new Week(13L,null);

    bookingLimit1 = new BookingLimit(new BookingLimitKey(
        user1.getId(),week1.getId()), 120, user1, week1);

    bookingLimit2 = new BookingLimit(new BookingLimitKey(
        user2.getId(),week2.getId()), 35, user2, week2);

    bookingLimit3 = new BookingLimit(new BookingLimitKey(
        user1.getId(),week3.getId()), 0, user1, week3);

    bookingLimit4 = new BookingLimit(new BookingLimitKey(
        user2.getId(),week3.getId()), 0, user2, week3);
  }

  static Arguments[] getBookingLimitIfExistTestArgs(){
    return new Arguments[]{
        Arguments.arguments(1L,week1,bookingLimit1),
        Arguments.arguments(2L,week2,bookingLimit2)
    };
  }

  @ParameterizedTest
  @MethodSource("getBookingLimitIfExistTestArgs")
  void getBookingLimitByInterviewerIfExistTest(Long interviewerId, Week week, BookingLimit expected)
      throws InvalidInterviewerException, NotInterviewerException {
    given(userService.getUserById(interviewerId)).willReturn(Optional.of(expected.getUser()));
    given(bookingLimitRepository.findById(expected.getId())).willReturn(Optional.of(expected));

    BookingLimit actual = cut.getBookingLimitByInterviewer(interviewerId,week);

    verify(bookingLimitRepository,never()).save(any());
    assertEquals(expected,actual);
  }

  static Arguments[] getBookingLimitIfNotExistTestArgs(){
    return new Arguments[]{
        Arguments.arguments(1L,week3,bookingLimit3),
        Arguments.arguments(2L,week3,bookingLimit4)
    };
  }

  @ParameterizedTest
  @MethodSource("getBookingLimitIfNotExistTestArgs")
  void getBookingLimitByInterviewerIfNotExistTest(Long interviewerId, Week week,
      BookingLimit expected) throws InvalidInterviewerException, NotInterviewerException {
    given(userService.getUserById(interviewerId)).willReturn(Optional.of(expected.getUser()));
    given(bookingLimitRepository.findById(expected.getId())).willReturn(Optional.empty());
    given(bookingLimitRepository.save(expected)).willReturn(expected);

    BookingLimit actual = cut.getBookingLimitByInterviewer(interviewerId,week);

    assertEquals(expected,actual);
  }

  static Arguments[] getBookingLimitInterviewerExceptionTestArgs(){
    return new Arguments[]{
        Arguments.arguments(4L,week1),
        Arguments.arguments(6L,week1)
    };
  }

  @ParameterizedTest
  @MethodSource("getBookingLimitInterviewerExceptionTestArgs")
  void getBookingLimitByInterviewerExceptionTest(Long interviewerId, Week week){
    given(userService.getUserById(interviewerId)).willReturn(Optional.empty());
    assertThrows(InvalidInterviewerException.class,
        () -> cut.getBookingLimitByInterviewer(interviewerId,week));
  }

  static Arguments[] createBookingLimitInterviewerExceptionTestArgs(){
    return new Arguments[]{
        Arguments.arguments(5L,123),
        Arguments.arguments(6L,14)
    };
  }

  @ParameterizedTest
  @MethodSource("createBookingLimitInterviewerExceptionTestArgs")
  void createBookingLimitInvalidInterviewerExceptionTest(Long interviewerId, Integer bookingLimit){
    given(userService.getUserById(interviewerId)).willReturn(Optional.empty());
    assertThrows(InvalidInterviewerException.class,
        () -> cut.createBookingLimit(interviewerId,bookingLimit));
  }

  static Arguments[] createBookingLimitNotInterviewerExceptionTestArgs(){
    return new Arguments[]{
        Arguments.arguments(user3.getId(),113,user3),
        Arguments.arguments(user4.getId(),17,user4)
    };
  }

  @ParameterizedTest
  @MethodSource("createBookingLimitNotInterviewerExceptionTestArgs")
  void createBookingLimitNotInterviewerExceptionTest(Long interviewerId, Integer bookingLimit,
      User user){
    given(userService.getUserById(interviewerId)).willReturn(Optional.of(user));
    assertThrows(NotInterviewerException.class,
        () -> cut.createBookingLimit(interviewerId,bookingLimit));
  }

  static Arguments[] createBookingLimitExceptionTestArgs(){
    return new Arguments[]{
        Arguments.arguments(user1.getId(),0,user1),
        Arguments.arguments(user2.getId(),-13,user2)
    };
  }

  @ParameterizedTest
  @MethodSource("createBookingLimitExceptionTestArgs")
  void createBookingLimitInvalidBookingLimitExceptionTest(Long interviewerId, Integer bookingLimit,
      User user){
    given(userService.getUserById(interviewerId)).willReturn(Optional.of(user));
    assertThrows(InvalidBookingLimitException.class,
        () -> cut.createBookingLimit(interviewerId,bookingLimit));
  }

  static Arguments[] createBookingLimitTestArgs(){
    return new Arguments[]{
        Arguments.arguments(1L,120,bookingLimit1),
        Arguments.arguments(2L,35,bookingLimit2)
    };
  }

  @ParameterizedTest
  @MethodSource("createBookingLimitTestArgs")
  void createBookingLimitTest(Long interviewerId, Integer bookingLimit, BookingLimit expected)
      throws InvalidInterviewerException, InvalidBookingLimitException, NotInterviewerException {
    given(userService.getUserById(interviewerId)).willReturn(Optional.of(expected.getUser()));
    given(weekService.getNextWeek()).willReturn(expected.getWeek());
    given(bookingLimitRepository.save(expected)).willReturn(expected);

    BookingLimit actual = cut.createBookingLimit(interviewerId,bookingLimit);

    verify(bookingLimitRepository).save(any());
    assertEquals(expected,actual);
  }

}
