package com.intellias.intellistart.interviewplanning.model.candidateslot;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * CandidateSlot entity.
 */
@Entity
@Table(name = "candidate_slots")
@Getter
@Setter
@RequiredArgsConstructor
public class CandidateSlot {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "candidate_slot_id")
  private Long id;

  private LocalDate date;

  private LocalTime from;

  private LocalTime to;

  // TODO: M:N with Booking
}
