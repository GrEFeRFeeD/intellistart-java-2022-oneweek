package com.intellias.intellistart.interviewplanning.model.week;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class WeekServiceTest {

  WeekRepository weekRepository = Mockito.mock(WeekRepository.class);
  WeekService weekService = new WeekService(weekRepository);

  @ParameterizedTest
  @CsvSource({"2022-01-01,0","2022-10-13,41","2023-01-01,52",
              "2023-01-02,53","2023-12-31,104","2024-01-01,105"})
  void getNumberOfWeekTest(LocalDate date,long expect){
    long result = weekService.getNumberOfWeek(date);
    assertEquals(expect,result);
  }


  @ParameterizedTest
  @CsvSource({"2023-01-02,MON","2024-02-13,TUE","2023-06-14,WED","2022-10-13,THU",
              "2024-03-15,FRI","2022-01-01,SAT","2023-01-01,SUN"})
  void getDayOfWeekTest(LocalDate date, DayOfWeek expect){
    DayOfWeek result = weekService.getDayOfWeek(date);
    assertEquals(expect,result);
  }

  @ParameterizedTest
  @CsvSource({"53,MON,2023-01-02","1,TUE,2022-01-04","41,WED,2022-10-12","173,THU,2025-04-17",
              "62,FRI,2023-03-10","273,SAT,2027-03-13","104,SUN,2023-12-31"})
  void convertToLocalDateTest(long numberOfWeek,DayOfWeek dayOfWeek,LocalDate expect){
    LocalDate resultDate = weekService.convertToLocalDate(numberOfWeek,dayOfWeek);
    assertEquals(expect,resultDate);
  }

 @Test
  void createWeekTest(){
    Week newWeek = new Week(153L,new HashSet<>());
    weekService.createWeek(153L);
    ArgumentCaptor<Week> weekArgumentCaptor = ArgumentCaptor.forClass(Week.class);
    verify(weekRepository).save(weekArgumentCaptor.capture());
    Week captureWeek = weekArgumentCaptor.getValue();
    assertEquals(newWeek,captureWeek);
 }

 @Test
  void getWeekByWeekNumIfNotExistTest(){
    Week expectWeek = new Week(153L,new HashSet<>());
    given(weekRepository.save(expectWeek)).willReturn(expectWeek);
    Week newWeek = weekService.getWeekByWeekNum(153L);
    verify(weekRepository).save(expectWeek);
    assertEquals(expectWeek,newWeek);
 }
  @Test
  void getWeekByWeekNumIfExistTest(){
    Week expectedWeek = new Week(153L,new HashSet<>());
    given(weekRepository.findById(153L)).willReturn(Optional.of(expectedWeek));
    Week newWeek = weekService.getWeekByWeekNum(153L);
    verify(weekRepository,never()).save(any());
    assertEquals(expectedWeek,newWeek);
  }

}
