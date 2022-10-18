package com.intellias.intellistart.interviewplanning.model.week;

import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.HashSet;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for Week entity.
 */
@Service
public class WeekService {

  private final WeekRepository weekRepository;

  @Autowired
  public WeekService(WeekRepository weekRepository) {
    this.weekRepository = weekRepository;
  }

  /**
   * Get date and convert it to number of week.
   *
   * @param date any date
   * @return number of week
   */
  public long getNumberOfWeek(LocalDate date) {
    long sumOfWeeks = 0;
    if (date.getYear() != 2022) {
      for (int i = 2022; i < date.getYear(); i++) {
        sumOfWeeks += date.withYear(i).range(WeekFields.ISO.weekOfYear()).getMaximum();
      }
    }
    return sumOfWeeks + date.get(WeekFields.ISO.weekOfYear());
  }

  /**
   * Get date and convert it to day of week.
   *
   * @param date any date
   * @return day of week
   */
  public DayOfWeek getDayOfWeek(LocalDate date) {
    String dayOfWeek = date.getDayOfWeek().toString().substring(0, 3);
    return DayOfWeek.valueOf(dayOfWeek);
  }

  /**
   * Get number of week and day of week
   * and convert them to date (LocalDate).
   *
   * @param weekNum number of week
   *
   * @param dayOfWeek day of week
   * @return date
   */
  public LocalDate convertToLocalDate(long weekNum, DayOfWeek dayOfWeek) {
    return LocalDate.now()
        .with(WeekFields.ISO.weekBasedYear(), getYear(weekNum))
        .with(WeekFields.ISO.weekOfYear(), getWeek(weekNum))
        .with(WeekFields.ISO.dayOfWeek(), dayOfWeek.ordinal() + 1L);
  }

  /**
   * Get number of week and return current year.
   *
   * @param weekNum number of week from 2022
   * @return current year
   */
  private long getYear(long weekNum) {
    return getFromWeekNum(weekNum, "year");
  }

  /**
   * Get number of week and calculate number of week from current year.
   *
   * @param weekNum number of week from 2022
   * @return number of week from current year
   */
  private long getWeek(long weekNum) {
    return getFromWeekNum(weekNum, "week");
  }

  /**
   * Get number of week from 2022 and string which shows what return, number of week
   * of current year or current year.
   *
   * @param weekNum number of week from 2022
   * @param type  type of returning
   * @return number of week of current year or current year
   */
  private long getFromWeekNum(long weekNum, String type) {
    LocalDate date = LocalDate.parse("2022-01-01");
    long newWeekNum = weekNum;
    int year = date.getYear();
    long yearWeekNum = date.range(WeekFields.ISO.weekOfYear()).getMaximum();
    while (newWeekNum > yearWeekNum) {
      year++;
      yearWeekNum = date.withYear(year).range(WeekFields.ISO.weekOfYear()).getMaximum();
      newWeekNum -= date.withYear(year - 1).range(WeekFields.ISO.weekOfYear()).getMaximum();
    }
    if (type.equals("year")) {
      return year;
    } else {
      return newWeekNum;
    }
  }

  /**
   * Return object Week for request for getting number of current week.
   *
   * @return Week object
   */
  public Week getCurrentWeek() {
    LocalDate date = LocalDate.now();
    return getWeekByWeekNum(getNumberOfWeek(date));
  }

  /**
   * Return object Week for request for getting number of next week.
   *
   * @return object Week
   */
  public Week getNextWeek() {
    LocalDate date = LocalDate.now();
    return getWeekByWeekNum(getNumberOfWeek(date) + 1L);
  }

  /**
   * Get number of week and check if object Week with id weekNum exists.
   * If it exists return this object if not object with such id is created and
   * also returned.
   *
   * @param weekNum number of week
   * @return object Week
   */
  public Week getWeekByWeekNum(Long weekNum) {
    Optional<Week> week = weekRepository.findById(weekNum);
    return week.orElseGet(() -> createWeek(weekNum));
  }

  /**
   * Create Week with id weekNum and return it.
   *
   * @param weekNum number of week
   * @return object Week
   */
  public Week createWeek(Long weekNum) {
    Week newWeek = new Week(weekNum, new HashSet<>());
    return weekRepository.save(newWeek);
  }
}
