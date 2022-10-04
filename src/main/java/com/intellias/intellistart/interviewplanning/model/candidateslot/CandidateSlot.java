package com.intellias.intellistart.interviewplanning.model.candidateslot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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

  /* TODO: Persist LocalTime/Date
  @Temporal(value = TemporalType.DATE)
  private Date date;

  @Temporal(value = TemporalType.TIME)
  private Date from;

  @Temporal(value = TemporalType.TIME)
  private Date to;
   */

  // TODO: M:N with Booking
}
