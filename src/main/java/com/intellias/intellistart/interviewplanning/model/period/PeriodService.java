package com.intellias.intellistart.interviewplanning.model.period;

import com.intellias.intellistart.interviewplanning.model.period.time.LocalTimeConverter;
import com.intellias.intellistart.interviewplanning.model.period.validation.PeriodValidator;
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

  @Autowired
  public PeriodService(PeriodRepository periodRepository, LocalTimeConverter timeConverter, PeriodValidator periodValidator) {
    this.periodRepository = periodRepository;
    this.timeConverter = timeConverter;
    this.periodValidator = periodValidator;
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

  private Period createPeriod(LocalTime from, LocalTime to){
    return null;
  }
}
