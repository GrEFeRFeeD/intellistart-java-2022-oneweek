package com.intellias.intellistart.interviewplanning.model.period;

import com.intellias.intellistart.interviewplanning.exceptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.model.period.services.TimeConverter;
import com.intellias.intellistart.interviewplanning.model.period.services.validation.PeriodValidator;
import java.time.LocalTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for Period entity.
 */
@Service
public class PeriodService {

  private final PeriodRepository periodRepository;
  private final TimeConverter timeConverter;
  private final PeriodValidator periodValidator;

  /**
   * Constructor.
   */
  @Autowired
  public PeriodService(PeriodRepository periodRepository,
      TimeConverter timeConverter,
      PeriodValidator periodValidator) {

    this.periodRepository = periodRepository;
    this.timeConverter = timeConverter;
    this.periodValidator = periodValidator;
  }


  /**
  * Alias for {@link #obtainPeriod(LocalTime, LocalTime)} with time conversion.
  *
  * @throws InvalidBoundariesException when parameters are invalid:
  *     can't be read as time
  *     wrong business logic
  */
  public Period obtainPeriod(String fromString, String toString) {
    LocalTime from = timeConverter.convert(fromString);
    LocalTime to = timeConverter.convert(toString);

    return obtainPeriod(from, to);
  }

  /**
   * Obtain period by "from" and "to": find if exists, create if not.
   *
   * @param from - LocalTime lower time boundary
   * @param to - LocalTime upper time boundary
   *
   * @throws InvalidBoundariesException when wrong business logic.
   */
  private Period obtainPeriod(LocalTime from, LocalTime to) {
    periodValidator.validate(from, to);

    Optional<Period> periodOptional = periodRepository.findPeriodByFromAndTo(from, to);

    return periodOptional.orElseGet(() -> periodRepository.save(
        new Period(null, from, to, null, null, null)));
  }

  /**
   * Tell if times of periods cross. Boundaries are inclusive.
   *
   * @return true if periods are overlapping.
   */
  public boolean areOverlapping(Period period1, Period period2) {
    LocalTime from1 = period1.getFrom();
    LocalTime from2 = period2.getFrom();

    return isTimeInPeriod(from1, period2)
        || isTimeInPeriod(from2, period1);
  }

  /**
   * Tell if given time isn't smaller than "from" and lower time is smaller than "to".
   */
  private boolean isTimeInPeriod(LocalTime time, Period period) {
    return time.compareTo(period.getFrom()) >= 0
        && time.compareTo(period.getTo()) < 0;
  }
}
