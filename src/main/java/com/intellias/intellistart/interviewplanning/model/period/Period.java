package com.intellias.intellistart.interviewplanning.model.period;

import com.intellias.intellistart.interviewplanning.model.booking.Booking;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlot;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Entity for period of time.
 */
@Entity
@Table(name = "periods")
@Getter
@Setter
@RequiredArgsConstructor
public class Period {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "period_id")
  private Long id;

  @Column(name = "period_from")
  private LocalTime from;

  @Column(name = "period_to")
  private LocalTime to;

  @OneToMany(mappedBy = "period")
  private Set<InterviewerSlot> interviewerSlots = new HashSet<>();

  @OneToMany(mappedBy = "period")
  private Set<CandidateSlot> candidateSlots = new HashSet<>();

  @OneToMany(mappedBy = "period")
  private Set<Booking> bookings = new HashSet<>();
}
