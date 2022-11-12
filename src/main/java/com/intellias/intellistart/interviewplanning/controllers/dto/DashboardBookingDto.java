package com.intellias.intellistart.interviewplanning.controllers.dto;

import com.intellias.intellistart.interviewplanning.model.booking.Booking;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DashboardBookingDto {

  private Long bookingId;
  private String subject;
  private String description;
  private Long interviewerSlotId;
  private Long candidateSlotId;
  private String from;
  private String to;

  public DashboardBookingDto(Booking booking) {
    this.bookingId = booking.getId();
    this.subject = booking.getSubject();
    this.description = booking.getDescription();
    this.interviewerSlotId = booking.getInterviewerSlot().getId();
    this.candidateSlotId = booking.getCandidateSlot().getId();
    this.from = booking.getPeriod().getFrom().toString();
    this.to = booking.getPeriod().getTo().toString();
  }
}
