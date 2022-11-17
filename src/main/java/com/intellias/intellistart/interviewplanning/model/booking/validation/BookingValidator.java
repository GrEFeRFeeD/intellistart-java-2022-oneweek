package com.intellias.intellistart.interviewplanning.model.booking.validation;

import com.intellias.intellistart.interviewplanning.exceptions.old.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.exceptions.old.InvalidDescriptionException;
import com.intellias.intellistart.interviewplanning.exceptions.old.InvalidSubjectException;
import com.intellias.intellistart.interviewplanning.exceptions.old.SlotsAreNotIntersectingException;
import com.intellias.intellistart.interviewplanning.model.booking.Booking;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlot;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.period.PeriodService;
import com.intellias.intellistart.interviewplanning.model.period.services.TimeService;
import com.intellias.intellistart.interviewplanning.model.week.WeekService;
import java.time.LocalDate;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Business logic Booking validator.
 */
@Component
public class BookingValidator {

  private static final int BOOKING_PERIOD_DURATION_MINUTES = 90;
  private static final int DESCRIPTION_MAX_SIZE = 4000;
  private static final int SUBJECT_MAX_SIZE = 255;
  private final PeriodService periodService;
  private final TimeService timeService;
  private final WeekService weekService;

  /**
   * Constructor.
   */
  @Autowired
  public BookingValidator(PeriodService periodService, TimeService timeService,
      WeekService weekService) {
    this.periodService = periodService;
    this.timeService = timeService;
    this.weekService = weekService;
  }

  /**
   * Perform business logic validation for updating Booking.
   *
   * @param updatingBooking Booking with old parameters
   * @param newDataBooking Booking with new parameters
   *
   * @throws InvalidBoundariesException       if duration of new Period is invalid
   * @throws SlotsAreNotIntersectingException if periods of InterviewSlot, CandidateSlot do not
   *                                          intersect with new Period or new Period is overlapping
   *                                          with existing Periods of InterviewerSlot and
   *                                          CandidateSlot
   */
  public void validateUpdating(Booking updatingBooking, Booking newDataBooking) {
    Period newPeriod = newDataBooking.getPeriod();

    int periodDuration = timeService.calculateDurationMinutes(
        newPeriod.getFrom(), newPeriod.getTo());
    if (periodDuration != BOOKING_PERIOD_DURATION_MINUTES) {
      throw new InvalidBoundariesException();
    }

    if (newDataBooking.getSubject().length() > SUBJECT_MAX_SIZE) {
      throw new InvalidSubjectException();
    }
    if (newDataBooking.getDescription().length() > DESCRIPTION_MAX_SIZE) {
      throw new InvalidDescriptionException();
    }

    InterviewerSlot newInterviewerSlot = newDataBooking.getInterviewerSlot();
    CandidateSlot newCandidateSlot = newDataBooking.getCandidateSlot();

    LocalDate interviewerSlotDate = weekService.convertToLocalDate(
        newInterviewerSlot.getWeek().getId(), newInterviewerSlot.getDayOfWeek());

    if (!interviewerSlotDate.equals(newCandidateSlot.getDate())) {
      throw new SlotsAreNotIntersectingException();
    }

    if (!periodService.isFirstInsideSecond(newPeriod, newInterviewerSlot.getPeriod())
        || !periodService.isFirstInsideSecond(newPeriod, newCandidateSlot.getPeriod())) {
      throw new SlotsAreNotIntersectingException();
    }

    Collection<Booking> interviewSlotBookings = newInterviewerSlot.getBookings();
    Collection<Booking> candidateSlotBookings = newCandidateSlot.getBookings();

    validatePeriodNotOverlappingWithOtherBookingPeriods(
        updatingBooking, newPeriod, interviewSlotBookings);

    validatePeriodNotOverlappingWithOtherBookingPeriods(
        updatingBooking, newPeriod, candidateSlotBookings);
  }

  private void validatePeriodNotOverlappingWithOtherBookingPeriods(
      Booking updatingBooking, Period period, Collection<Booking> bookings) {

    for (Booking booking : bookings) {
      if (periodService.areOverlapping(booking.getPeriod(), period)
          && !booking.equals(updatingBooking)) {
        throw new SlotsAreNotIntersectingException();
      }
    }
  }

  /**
   * Alias for {@link #validateUpdating(Booking, Booking)}.
   */
  public void validateCreating(Booking newBooking) {
    validateUpdating(newBooking, newBooking);
  }
}
