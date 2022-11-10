package com.intellias.intellistart.interviewplanning.model.bookinglimit;

import com.intellias.intellistart.interviewplanning.exceptions.InvalidBookingLimitException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidInterviewerException;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.user.UserService;
import com.intellias.intellistart.interviewplanning.model.week.Week;
import com.intellias.intellistart.interviewplanning.model.week.WeekService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for BookingLimit entity.
 */
@Service
public class BookingLimitService {

  private final BookingLimitRepository bookingLimitRepository;
  private final WeekService weekService;
  private final UserService userService;

  /**
   * Constructor for BookingLimitService.
   *
   * @param bookingLimitRepository - bookingLimitRepository
   * @param weekService - weekService
   * @param userService - userService
   */
  @Autowired
  public BookingLimitService(BookingLimitRepository bookingLimitRepository, WeekService weekService,
      UserService userService) {
    this.bookingLimitRepository = bookingLimitRepository;
    this.weekService = weekService;
    this.userService = userService;
  }

  public BookingLimit getBookingLimitForNextWeek(Long interviewerId)
      throws InvalidInterviewerException {
    return getBookingLimitByInterviewer(interviewerId, weekService.getNextWeek());
  }

  public BookingLimit getBookingLimitForCurrentWeek(Long interviewerId)
      throws InvalidInterviewerException {
    return getBookingLimitByInterviewer(interviewerId, weekService.getCurrentWeek());
  }

  /**
   * Return booking limit of certain interviewer for certain week,
   * or create infinite booking limit.
   *
   * @param interviewerId - interviewer id
   * @param week - certain week
   * @return BookingLimit
   * @throws InvalidInterviewerException - invalid user (interviewer) exception
   */
  public BookingLimit getBookingLimitByInterviewer(Long interviewerId, Week week)
      throws InvalidInterviewerException {

    User user = userService.getUserById(interviewerId)
        .orElseThrow(InvalidInterviewerException::new);

    Long weekNum = week.getId();

    Optional<BookingLimit> bookingLimit = bookingLimitRepository
        .findById(new BookingLimitKey(interviewerId, weekNum));

    while (weekNum > 0) {
      if (bookingLimit.isPresent()) {
        return bookingLimit.get();
      }
      weekNum--;
    }

    return createInfiniteBookingLimit(user, week);
  }

  private BookingLimit createInfiniteBookingLimit(User user, Week week) {
    return bookingLimitRepository.save(new BookingLimit(new BookingLimitKey(user.getId(),
        week.getId()), 0, user, week));
  }

  /**
   * Create BookingLimit for next week.
   *
   * @param interviewerId - user id
   * @param bookingLimit - booking limit
   * @return BookingLimit
   * @throws InvalidInterviewerException - invalid user (interviewer) exception
   */
  public BookingLimit createBookingLimit(Long interviewerId, Integer bookingLimit)
      throws InvalidInterviewerException, InvalidBookingLimitException {

    User user = userService.getUserById(interviewerId)
        .orElseThrow(InvalidInterviewerException::new);

    if (bookingLimit <= 0) {
      throw new InvalidBookingLimitException();
    }

    Week nextWeek = weekService.getNextWeek();

    BookingLimit newBookingLimit = new BookingLimit(
        new BookingLimitKey(interviewerId, nextWeek.getId()),
        bookingLimit, user, nextWeek);

    return bookingLimitRepository.save(newBookingLimit);
  }
}