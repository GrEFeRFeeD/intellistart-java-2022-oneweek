package com.intellias.intellistart.interviewplanning.controllers.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.intellias.intellistart.interviewplanning.model.booking.Booking;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlot;
import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.week.WeekService;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardMapDto {

  @JsonIgnore
  private Long weekNum;
  @JsonIgnore
  private final WeekService weekService;
  private Map<LocalDate, DashboardDto> dashboard;

  public DashboardMapDto(Long weekNum, WeekService weekService) {
    this.dashboard = new HashMap<>();
    this.weekNum = weekNum;
    this.weekService = weekService;

    for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
      LocalDate date = weekService.convertToLocalDate(weekNum, dayOfWeek);
      dashboard.put(date, new DashboardDto());
    }
  }

  public void addInterviewerSlots(Set<InterviewerSlot> interviewerSlots) {

    for (InterviewerSlot interviewerSlot : interviewerSlots) {

      dashboard.get(weekService.convertToLocalDate(weekNum, interviewerSlot.getDayOfWeek()))
          .getInterviewerSlots()
          .add(new DashboardInterviewerSlotDto(interviewerSlot));

      Map<Long, DashboardBookingDto> bookingDtoMap = interviewerSlot.getBookings().stream()
          .collect(Collectors.toMap(Booking::getId, DashboardBookingDto::new));

      dashboard.get(weekService.convertToLocalDate(weekNum, interviewerSlot.getDayOfWeek()))
          .getBookings()
          .putAll(bookingDtoMap);
    }
  }

  public void addCandidateSlots(Set<CandidateSlot> candidateSlots) {

    for (CandidateSlot candidateSlot: candidateSlots) {

      dashboard.get(candidateSlot.getDate())
          .getCandidateSlots()
          .add(new DashboardCandidateSlotDto(candidateSlot));

      Map<Long, DashboardBookingDto> bookingDtoMap = candidateSlot.getBookings().stream()
          .collect(Collectors.toMap(Booking::getId, DashboardBookingDto::new));

      dashboard.get(candidateSlot.getDate())
          .getBookings()
          .putAll(bookingDtoMap);;
    }
  }
}
