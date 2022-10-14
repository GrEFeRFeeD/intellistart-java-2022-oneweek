package com.intellias.intellistart.interviewplanning.model.period;

import com.intellias.intellistart.interviewplanning.exeptions.SlotIsOverlappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for Period entity.
 */
@Service
public class PeriodService {

  private final PeriodRepository periodRepository;

  @Autowired
  public PeriodService(PeriodRepository periodRepository) {
    this.periodRepository = periodRepository;
  }

  public Period getPeriod(String from, String to) {
    return null;
  }

  public boolean isOverlap(Period p1, Period p2) {
    return false;
  }
}
