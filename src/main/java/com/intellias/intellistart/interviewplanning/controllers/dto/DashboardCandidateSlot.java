package com.intellias.intellistart.interviewplanning.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.intellias.intellistart.interviewplanning.model.booking.Booking;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlot;
import java.time.LocalDate;
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
public class DashboardCandidateSlot {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
  private LocalDate date;
  private String from;
  private String to;
  private String candidateEmail;
  private String candidateName;
  private Set<Long> bookings;

  public DashboardCandidateSlot(CandidateSlot candidateSlot) {
    this.date = candidateSlot.getDate();
    this.from = candidateSlot.getPeriod().getFrom().toString();
    this.to = candidateSlot.getPeriod().getTo().toString();
    this.candidateEmail = candidateSlot.getEmail();
    this.candidateName = candidateSlot.getName();

    this.bookings = candidateSlot.getBookings().stream()
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
    DashboardCandidateSlot that = (DashboardCandidateSlot) o;
    return Objects.equals(date, that.date) && Objects.equals(from, that.from)
        && Objects.equals(to, that.to) && Objects.equals(candidateEmail,
        that.candidateEmail) && Objects.equals(candidateName, that.candidateName)
        && Objects.equals(bookings, that.bookings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date, from, to, candidateEmail, candidateName, bookings);
  }
}
