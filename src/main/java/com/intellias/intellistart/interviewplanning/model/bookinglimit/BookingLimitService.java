package com.intellias.intellistart.interviewplanning.model.bookinglimit;

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

  /**
   * Return booking limit or create unlimited booking limit.
   *
   * @param interviewerId - user id
   * @param weekNum - number of week
   * @return BookingLimit
   * @throws InvalidInterviewerException - invalid user (interviewer) exception
   */
  public BookingLimit getBookingLimitByInterviewer(Long interviewerId, Long weekNum)
      throws InvalidInterviewerException {
    Optional<BookingLimit> bookingLimit = bookingLimitRepository
        .findById(new BookingLimitKey(interviewerId, weekNum));

    if (bookingLimit.isPresent()) {
      return bookingLimit.get();
    } else if (weekNum > 0) {
      return getBookingLimitByInterviewer(interviewerId, weekNum - 1);
    }
    return createInfiniteBookingLimit(interviewerId, weekService.getCurrentWeek());
  }

  private BookingLimit createInfiniteBookingLimit(Long interviewerId, Week currentWeek)
      throws InvalidInterviewerException {
    User user = userService.getUserById(interviewerId)
        .orElseThrow(InvalidInterviewerException::new);
    return new BookingLimit(new BookingLimitKey(interviewerId, currentWeek.getId()),
        0, user, currentWeek);
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
      throws InvalidInterviewerException {
    User user = userService.getUserById(interviewerId)
        .orElseThrow(InvalidInterviewerException::new);

    Week nextWeekNum = weekService.getNextWeek();

    BookingLimit newBookingLimit = new BookingLimit(
        new BookingLimitKey(interviewerId, nextWeekNum.getId()),
        bookingLimit, user, nextWeekNum);

    return bookingLimitRepository.save(newBookingLimit);
  }
}