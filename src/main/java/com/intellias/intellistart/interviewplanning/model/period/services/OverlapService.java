package com.intellias.intellistart.interviewplanning.model.period.services;

import com.intellias.intellistart.interviewplanning.model.period.Period;
import java.time.LocalTime;
import org.springframework.stereotype.Component;

/**
 * Service to check if times of periods cross.
 */
@Component
public class OverlapService {

  /**
   * Check method.
   *
   * @return true if periods overlap.
   */
  public boolean isOverlap(Period period1, Period period2) {
    LocalTime from1 = period1.getFrom();
    LocalTime from2 = period2.getFrom();

    return isTimeInPeriod(from1, period2)
        || isTimeInPeriod(from2, period1);
  }

  private boolean isTimeInPeriod(LocalTime time, Period period) {
    return time.isAfter(period.getFrom()) && period.getTo().isAfter(time);
  }
}
