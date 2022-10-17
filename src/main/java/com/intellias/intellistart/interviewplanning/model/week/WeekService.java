package com.intellias.intellistart.interviewplanning.model.week;

import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.HashSet;
import java.util.Locale;
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
   * Method getNumberOfWeek() gets date and convert it to number of week.
   *
   * @param date any date
   * @return number of week
   */
  public long getNumberOfWeek(LocalDate date) {
    WeekFields weekFields = WeekFields.of(Locale.getDefault());
    long sumOfWeeks = 0;
    if (date.getYear() != 2022) {
      for (int i = 2022; i < date.getYear(); i++) {
        sumOfWeeks += date.withYear(i).range(weekFields.weekOfYear()).getMaximum();
      }
    }
    return sumOfWeeks + date.get(weekFields.weekOfYear());
  }

  public DayOfWeek getDayOfWeek(LocalDate date) {
    String dayOfWeek = date.getDayOfWeek().toString().substring(0, 3);
    return DayOfWeek.valueOf(dayOfWeek);
  }

  /**
   * Method convertToLocalDate() gets number of week and day of week
   * after that convert they to date (LocalDate).
   *
   * @param weekNum number of week
   *
   * @param dayOfWeek day of week
   * @return date
   */
  public LocalDate convertToLocalDate(long weekNum, DayOfWeek dayOfWeek) {
    WeekFields weekFields = WeekFields.of(Locale.getDefault());
    return LocalDate.now()
        .with(weekFields.weekBasedYear(), getYear(weekNum))
        .with(weekFields.weekOfYear(), getWeek(weekNum))
        .with(weekFields.dayOfWeek(), dayOfWeek.ordinal() + 1L);
  }

  private long getYear(long weekNum) {
    return getFromWeekNum(weekNum, "year");
  }

  private long getWeek(long weekNum) {
    return getFromWeekNum(weekNum, "week");
  }

  private long getFromWeekNum(long weekNum, String type) {
    LocalDate date = LocalDate.parse("2022-01-01");
    WeekFields weekFields = WeekFields.of(Locale.getDefault());
    long newWeekNum = weekNum;
    int year = date.getYear();
    long yearWeekNum = date.range(weekFields.weekOfYear()).getMaximum();
    while (newWeekNum > yearWeekNum) {
      year++;
      yearWeekNum = date.withYear(year).range(weekFields.weekOfYear()).getMaximum();
      newWeekNum -= date.withYear(year - 1).range(weekFields.weekOfYear()).getMaximum();
    }
    if (type.equals("year")) {
      return year;
    } else {
      return newWeekNum;
    }
  }

  public Week getCurrentWeek() {
    LocalDate date = LocalDate.now();
    return getWeekByWeekNum(getNumberOfWeek(date));
  }

  public Week getNextWeek() {
    LocalDate date = LocalDate.now();
    return getWeekByWeekNum(getNumberOfWeek(date) + 1L);
  }

  public Week getWeekByWeekNum(Long weekNum) {
    Optional<Week> week = weekRepository.findById(weekNum);
    return week.orElseGet(() -> createWeek(weekNum));
  }

  public Week createWeek(Long weekNum) {
    Week newWeek = new Week(weekNum, new HashSet<>());
    return weekRepository.save(newWeek);
  }
}
