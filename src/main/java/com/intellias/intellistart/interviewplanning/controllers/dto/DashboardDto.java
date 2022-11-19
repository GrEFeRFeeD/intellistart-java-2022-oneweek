package com.intellias.intellistart.interviewplanning.controllers.dto;

import com.intellias.intellistart.interviewplanning.model.booking.Booking;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Dto object for representation of all slots and bookings per certain time period.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DashboardDto {

  private Set<DashboardInterviewerSlotDto> interviewerSlots = new HashSet<>();
  private Set<DashboardCandidateSlotDto> candidateSlots = new HashSet<>();
  private Map<Long, DashboardBookingDto> bookings = new HashMap<>();
}

