package com.intellias.intellistart.interviewplanning.model.period;

import java.time.LocalTime;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for Period entity.
 */
@Service
public class PeriodService {

  private static  PeriodRepository periodRepository;

  @Autowired
  public PeriodService(PeriodRepository periodRepository) {
    this.periodRepository = periodRepository;
  }
  public static Period  getPeriod(String x, String v){ //заглушка
       return null;//new Period(null, LocalTime.of(10, 0), LocalTime.of(20, 0), null, null, null);
  }

}
