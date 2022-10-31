package com.intellias.intellistart.interviewplanning.model.period.services;

import com.intellias.intellistart.interviewplanning.model.period.Period;
import java.time.LocalTime;
import org.springframework.stereotype.Service;

/**
 * Service to check if times of periods cross.
 */
@Service
public class OverlapService {

  /**
   * Check if times of periods cross. Boundaries are exclusive.
   *
   * @return true if periods overlap.
   */
  public boolean isOverlap(Period period1, Period period2) {
    LocalTime from1 = period1.getFrom();
    LocalTime from2 = period2.getFrom();

    return isTimeInPeriod(from1, period2)
        || isTimeInPeriod(from2, period1);
  }

  /**
   * Check if given time is bigger than period's start time and lower time is smaller then end
   * period's end time.
   */
  private boolean isTimeInPeriod(LocalTime time, Period period) {
    return time.compareTo(period.getFrom()) >= 0
        && time.compareTo(period.getTo()) < 0;
  }
}
