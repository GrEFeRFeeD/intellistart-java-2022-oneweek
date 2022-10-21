package com.intellias.intellistart.interviewplanning.model.week;

import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for Week entity.
 */
@Service
public class WeekService {

  private static  WeekRepository weekRepository;



  @Autowired
  public WeekService(WeekRepository weekRepository) {
    this.weekRepository = weekRepository;
  }

  public static DayOfWeek getDayOfWeek(LocalDate date) {
    String dayOfWeek = date.getDayOfWeek().toString().substring(0, 3);
    return DayOfWeek.valueOf(dayOfWeek);
  }

  public static Week getWeekByWeekNum(Long weekNum) {
    Optional<Week> week = weekRepository.findById(weekNum);
    return week.orElseGet(() -> createWeek(weekNum));
  }


  public static Week createWeek(Long weekNum) {
    Week newWeek = new Week(weekNum, new HashSet<>());
    return weekRepository.save(newWeek);
  }
}
