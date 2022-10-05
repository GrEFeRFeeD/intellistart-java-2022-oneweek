package com.intellias.intellistart.interviewplanning.model.booking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlot;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
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

  @ManyToOne
  @JoinColumn(name = "period_id")
  private Period period;

  private String subject;

  private String description;

  @ManyToOne
  @MapsId("interviewerSlotId")
  @JoinColumn(name = "interviewer_slot_id")
  @JsonIgnore
  private InterviewerSlot interviewerSlot;

  @ManyToOne
  @MapsId("candidateSlotId")
  @JoinColumn(name = "candidate_slot_id")
  @JsonIgnore
  private CandidateSlot candidateSlot;

}
