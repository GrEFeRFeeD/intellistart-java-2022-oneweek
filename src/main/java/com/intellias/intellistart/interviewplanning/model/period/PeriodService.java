package com.intellias.intellistart.interviewplanning.model.period;

import com.intellias.intellistart.interviewplanning.exceptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.model.period.services.OverlapService;
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
  private final OverlapService overlapService;

  /**
   * Constructor.
   */
  @Autowired
  public PeriodService(PeriodRepository periodRepository,
      TimeConverter timeConverter,
      PeriodValidator periodValidator,
      OverlapService overlapService) {

    this.periodRepository = periodRepository;
    this.timeConverter = timeConverter;
    this.periodValidator = periodValidator;
    this.overlapService = overlapService;
  }


  /**
  * Get period by given parameters.
  * If needed period exists obtain it,
  * if not exists - create it
  *
  * @param fromString - String representation of lower time boundary
  * @param toString - String representation of upper time boundary
  *
  * @throws InvalidBoundariesException when parameters are incorrect:
  *     can't be read as time
  *     wrong business logic
  */
  public Period getPeriod(String fromString, String toString) {
    LocalTime from = timeConverter.convert(fromString);
    LocalTime to = timeConverter.convert(toString);

    periodValidator.validate(from, to);

    Optional<Period> periodOptional = periodRepository.findPeriodByFromAndTo(from, to);

    if (periodOptional.isPresent()) {
      return periodOptional.get();
    }
    return createPeriod(from, to);
  }

  public boolean isOverlap(Period period1, Period period2) {
    return overlapService.isOverlap(period1, period2);
  }

  /**
   * Create period by from and to.
   * Business logic of parameters is correct.
   */
  Period createPeriod(LocalTime from, LocalTime to) {
    //TODO decide if remove double links between periods and slots

    return periodRepository.save(
        new Period(null, from, to, null, null, null));
  }
  public Period getPeriodById(Long id){
    return periodRepository.findById(id).get();
  }

}
