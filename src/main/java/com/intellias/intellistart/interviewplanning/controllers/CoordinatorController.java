package com.intellias.intellistart.interviewplanning.controllers;

import com.intellias.intellistart.interviewplanning.controllers.dto.DashboardDto;
import com.intellias.intellistart.interviewplanning.controllers.dto.DashboardInterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.booking.Booking;
import com.intellias.intellistart.interviewplanning.model.bookinglimit.BookingLimitService;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlot;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlotService;
import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService;
import com.intellias.intellistart.interviewplanning.model.week.Week;
import com.intellias.intellistart.interviewplanning.model.week.WeekService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for processing requests from users with Coordinator role.
 */
@RestController
public class CoordinatorController {

  private final WeekService weekService;
  private final BookingLimitService bookingLimitService;
  private final InterviewerSlotService interviewerSlotService;
  private final CandidateSlotService candidateSlotService;

  public CoordinatorController(WeekService weekService, BookingLimitService bookingLimitService,
      InterviewerSlotService interviewerSlotService, CandidateSlotService candidateSlotService) {
    this.weekService = weekService;
    this.bookingLimitService = bookingLimitService;
    this.interviewerSlotService = interviewerSlotService;
    this.candidateSlotService = candidateSlotService;
  }

  @GetMapping("/weeks/{weekId}/dashboard")
  public ResponseEntity<?> getDashboard(@PathVariable("weekId") Long weekId) {

    Week week = weekService.getWeekByWeekNum(weekId);

    Set<InterviewerSlot> interviewerSlots = interviewerSlotService.getSlotsByWeek(week);

    Map<DayOfWeek, DashboardDto> dashboard = initializeDashboard(interviewerSlots);



    return ResponseEntity.ok(dashboard);
  }

  public Map<DayOfWeek, DashboardDto> initializeDashboard
      (Set<InterviewerSlot> interviewerSlots) {

    Map<DayOfWeek, DashboardDto> dashboard = new HashMap<>();

    for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
      dashboard.put(dayOfWeek, new DashboardDto());
    }

    for (InterviewerSlot interviewerSlot : interviewerSlots) {

      dashboard.get(interviewerSlot.getDayOfWeek())
          .getInterviewerSlots()
          .add(new DashboardInterviewerSlot(interviewerSlot));

      Map<Long, Booking> bookingMap = interviewerSlot.getBookings().stream()
          .collect(Collectors.toMap(Booking::getId, b -> b));

      dashboard.get(interviewerSlot.getDayOfWeek())
          .getBookings()
          .putAll(bookingMap);
    }

    return dashboard;
  }
}
