package com.intellias.intellistart.interviewplanning.model.booking;

import com.intellias.intellistart.interviewplanning.exceptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotsAreNotIntersectingException;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlot;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.period.PeriodService;
import com.intellias.intellistart.interviewplanning.model.period.services.TimeService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Business logic Booking validator.
 */
public class BookingValidator {

  private static final int BOOKING_PERIOD_DURATION_MINUTES = 90;
  private final PeriodService periodService;
  private final TimeService timeService;

  @Autowired
  public BookingValidator(PeriodService periodService, TimeService timeService) {
    this.periodService = periodService;
    this.timeService = timeService;
  }

  /**
   * Perform business logic validation of Booking parameters.
   *
   * @param oldBooking Booking with old parameters
   *
   * @throws InvalidBoundariesException if duration of new Period is invalid
   * @throws SlotsAreNotIntersectingException if
   *     periods of InterviewSlot, CandidateSlot do not intersect with new Period or
   *     new Period is overlapping with existing Periods of InterviewerSlot and CandidateSlot
   */
  public void validateParameters(
      Booking oldBooking,
      InterviewerSlot interviewerSlot,
      CandidateSlot candidateSlot,
      Period newPeriod) {

    int periodDuration = timeService.calculateDurationMinutes(
        newPeriod.getFrom(), newPeriod.getTo());
    if (periodDuration != BOOKING_PERIOD_DURATION_MINUTES) {
      throw new InvalidBoundariesException();
    }

    if (!periodService.isFirstInsideSecond(newPeriod, interviewerSlot.getPeriod())
        || !periodService.isFirstInsideSecond(newPeriod, candidateSlot.getPeriod())) {
      throw new SlotsAreNotIntersectingException();
    }

    Collection<Booking> interviewSlotBookings = interviewerSlot.getBookings();
    Collection<Booking> candidateSlotBookings = candidateSlot.getBookings();

    validatePeriodNotOverlappingWithOtherBookingPeriods(
        oldBooking, newPeriod, interviewSlotBookings);

    validatePeriodNotOverlappingWithOtherBookingPeriods(
        oldBooking, newPeriod, candidateSlotBookings);
  }

  private void validatePeriodNotOverlappingWithOtherBookingPeriods(
      Booking updatingBooking, Period period, Collection<Booking> bookings) {

    for (Booking booking : bookings) {
      if (periodService.areOverlapping(booking.getPeriod(), period)
          && !updatingBooking.equals(booking)) {
        throw new SlotsAreNotIntersectingException();
      }
    }
  }
}
