package com.intellias.intellistart.interviewplanning.model.interviewerslot;

import com.intellias.intellistart.interviewplanning.exceptions.SlotIsNotFoundException;
import com.intellias.intellistart.interviewplanning.model.booking.BookingService;
import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.week.Week;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Service for InterviewSlot entity.
 */
@Service
public class InterviewerSlotService {

  private final InterviewerSlotRepository interviewerSlotRepository;
  private final BookingService bookingService;

  /**
   * Constructor for InterviewerSlotService.
   *
   * @param interviewerSlotRepository - interviewerSlotRepository
   */
  @Autowired
  public InterviewerSlotService(
      InterviewerSlotRepository interviewerSlotRepository, BookingService bookingService) {
    this.interviewerSlotRepository = interviewerSlotRepository;
    this.bookingService = bookingService;
  }

  /**
   * Get Optional of InterviewerSlot from database.
   *
   * @param id - Long id of InterviewerSlot to find
   *
   * @return {@link Optional} of {@link InterviewerSlot}
   */
  public InterviewerSlot getSlotById(Long id) throws SlotIsNotFoundException {
    return interviewerSlotRepository.findById(id).orElseThrow(SlotIsNotFoundException::new);
  }

  /**
   * Get InterviewerSlot and save it in the DB.
   *
   * @param interviewerSlot - interviewerSlot
   * @return InterviewerSlot
   */
  public InterviewerSlot create(InterviewerSlot interviewerSlot) {
    return interviewerSlotRepository.save(interviewerSlot);
  }

  /**
   * Method deletes all slots of the given user,
   * before deleting a slot it deletes all bookings in the slots being deleted.
   *
   * @param user - user by which slots are deleted.
   */
  public void deleteSlotsByUser(User user) {
    List<InterviewerSlot> interviewerSlots =
        interviewerSlotRepository.findInterviewerSlotsByUser(user);

    for (InterviewerSlot interviewerSlot : interviewerSlots) {
      if (interviewerSlot.getBookings() != null && !interviewerSlot.getBookings().isEmpty()) {
        bookingService.deleteBookings(interviewerSlot.getBookings());
      }
    }

    interviewerSlotRepository.deleteAll(interviewerSlots);
  }

  /**
   * Get slots of user by weekId.
   *
   * @param userEmail - userEmail
   * @param weekId - weekId
   * @return {@link List} of {@link InterviewerSlot}
   */
  public List<InterviewerSlot> getSlotsByWeek(String userEmail, Long weekId) {
    return interviewerSlotRepository.getInterviewerSlotsByUserEmailAndWeekId(userEmail, weekId);
  }
}
