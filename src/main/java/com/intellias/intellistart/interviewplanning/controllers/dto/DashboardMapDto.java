package com.intellias.intellistart.interviewplanning.controllers.dto;

import com.intellias.intellistart.interviewplanning.model.booking.Booking;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlot;
import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardMapDto {

  private Map<DayOfWeek, DashboardDto> dashboardDtoMap;

  public DashboardMapDto() {
    this.dashboardDtoMap = new HashMap<>();

    for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
      dashboardDtoMap.put(dayOfWeek, new DashboardDto());
    }
  }

  public void addInterviewerSlots(Set<InterviewerSlot> interviewerSlots) {

    for (InterviewerSlot interviewerSlot : interviewerSlots) {

      dashboardDtoMap.get(interviewerSlot.getDayOfWeek())
          .getInterviewerSlots()
          .add(new DashboardInterviewerSlot(interviewerSlot));

      Map<Long, Booking> bookingMap = interviewerSlot.getBookings().stream()
          .collect(Collectors.toMap(Booking::getId, b -> b));

      dashboardDtoMap.get(interviewerSlot.getDayOfWeek())
          .getBookings()
          .putAll(bookingMap);
    }
  }

  public void addCandidateSlots(Set<CandidateSlot> candidateSlots) {

    for (CandidateSlot candidateSlot: candidateSlots) {

      DayOfWeek dayOfWeek = DayOfWeek.valueOf(candidateSlot
          .getDate()
          .getDayOfWeek()
          .toString()
          .substring(0, 3));

      dashboardDtoMap.get(dayOfWeek)
          .getCandidateSlots()
          .add(new DashboardCandidateSlot(candidateSlot));

      Map<Long, Booking> bookingMap = candidateSlot.getBookings().stream()
          .collect(Collectors.toMap(Booking::getId, b -> b));

      dashboardDtoMap.get(dayOfWeek)
          .getBookings()
          .putAll(bookingMap);;
    }
  }
}
