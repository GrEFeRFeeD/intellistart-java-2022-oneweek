package com.intellias.intellistart.interviewplanning.model.booking.validation;

import com.intellias.intellistart.interviewplanning.exceptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidDescriptionException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidSubjectException;
import com.intellias.intellistart.interviewplanning.exceptions.SlotsAreNotIntersectingException;
import com.intellias.intellistart.interviewplanning.model.booking.Booking;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlot;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.period.PeriodService;
import com.intellias.intellistart.interviewplanning.model.period.services.TimeService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Business logic Booking validator.
 */
@Component
public class BookingDataValidator {
  //TODO : fix javadoc
  private static final int BOOKING_PERIOD_DURATION_MINUTES = 90;
  private static final int DESCRIPTION_MAX_SIZE = 4000;
  private static final int SUBJECT_MAX_SIZE = 255;
  private final PeriodService periodService;
  private final TimeService timeService;

  @Autowired
  public BookingDataValidator(PeriodService periodService, TimeService timeService) {
    this.periodService = periodService;
    this.timeService = timeService;
  }

  /**
   * Perform business logic validation of Booking parameters.
   *
   * @param updatingBooking Booking with old parameters
   *
   * @throws InvalidBoundariesException if duration of new Period is invalid
   * @throws SlotsAreNotIntersectingException if
   *     periods of InterviewSlot, CandidateSlot do not intersect with new Period or
   *     new Period is overlapping with existing Periods of InterviewerSlot and CandidateSlot
   */
  public void validate(Booking updatingBooking, BookingData newData) {
    Period newPeriod = newData.getPeriod();

    int periodDuration = timeService.calculateDurationMinutes(
        newPeriod.getFrom(), newPeriod.getTo());
    if (periodDuration != BOOKING_PERIOD_DURATION_MINUTES) {
      throw new InvalidBoundariesException();
    }

    InterviewerSlot newInterviewerSlot = newData.getInterviewerSlot();
    CandidateSlot newCandidateSlot = newData.getCandidateSlot();

    if (!periodService.isFirstInsideSecond(newPeriod, newInterviewerSlot.getPeriod())
        || !periodService.isFirstInsideSecond(newPeriod, newCandidateSlot.getPeriod())) {
      throw new SlotsAreNotIntersectingException();
    }

    if (newData.getSubject().length() <= SUBJECT_MAX_SIZE) {
      throw new InvalidSubjectException();
    }
    if (newData.getDescription().length() <= DESCRIPTION_MAX_SIZE) {
      throw new InvalidDescriptionException();
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
          && !updatingBooking.equals(booking)) {
        throw new SlotsAreNotIntersectingException();
      }
    }
  }
}
