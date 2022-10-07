package com.intellias.intellistart.interviewplanning.model.period;

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
}
