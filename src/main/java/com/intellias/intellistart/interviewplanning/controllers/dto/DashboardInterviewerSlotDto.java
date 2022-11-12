package com.intellias.intellistart.interviewplanning.controllers.dto;

import com.intellias.intellistart.interviewplanning.model.booking.Booking;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DashboardInterviewerSlotDto {

  private String from;
  private String to;
  private Long interviewerSlotId;
  private Long interviewerId;
  private Set<Long> bookings;

  public DashboardInterviewerSlotDto(InterviewerSlot interviewerSlot) {
    this.interviewerSlotId = interviewerSlot.getId();
    this.interviewerId = interviewerSlot.getUser().getId();
    this.from = interviewerSlot.getPeriod().getFrom().toString();
    this.to = interviewerSlot.getPeriod().getTo().toString();

    this.bookings = interviewerSlot.getBookings().stream()
        .map(Booking::getId)
        .collect(Collectors.toSet());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DashboardInterviewerSlotDto that = (DashboardInterviewerSlotDto) o;
    return Objects.equals(interviewerSlotId, that.interviewerSlotId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(interviewerSlotId);
  }
}
