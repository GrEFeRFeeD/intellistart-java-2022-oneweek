package com.intellias.intellistart.interviewplanning.model.booking;

import java.time.LocalTime;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Booking entity.
 */
@Entity
@Table(name = "bookings")
@Getter
@Setter
@RequiredArgsConstructor
public class Booking {

  @EmbeddedId
  private BookingKey id;

  private LocalTime from;

  private LocalTime to;

  private String subject;

  private String description;

  // TODO: N:M with InterviewerSlot
  // TODO: N:M with CandidateSlot

}
