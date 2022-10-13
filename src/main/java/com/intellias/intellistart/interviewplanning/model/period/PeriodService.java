package com.intellias.intellistart.interviewplanning.model.period;

import com.intellias.intellistart.interviewplanning.model.period.services.OverlapService;
import com.intellias.intellistart.interviewplanning.model.period.services.LocalTimeConverter;
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
  private final LocalTimeConverter timeConverter;
  private final PeriodValidator periodValidator;

  private final OverlapService overlapService;

  @Autowired
  public PeriodService(PeriodRepository periodRepository,
      LocalTimeConverter timeConverter,
      PeriodValidator periodValidator,
      OverlapService overlapService) {

    this.periodRepository = periodRepository;
    this.timeConverter = timeConverter;
    this.periodValidator = periodValidator;
    this.overlapService = overlapService;
  }

  public Period getPeriod(String fromString, String toString){
    LocalTime from = timeConverter.convert(fromString);
    LocalTime to = timeConverter.convert(toString);

    periodValidator.validate(from, to);

    Optional<Period> periodOptional = periodRepository.find(from, to);

    if(periodOptional.isPresent()){
      return periodOptional.get();
    }

    return createPeriod(from, to);
  }

  public boolean isOverlap(Period period1, Period period2){
    return overlapService.isOverlap(period1, period2);
  }

  private Period createPeriod(LocalTime from, LocalTime to){
    return null;
  }
}
