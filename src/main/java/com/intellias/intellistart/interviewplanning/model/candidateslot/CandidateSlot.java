package com.intellias.intellistart.interviewplanning.model.candidateslot;

import com.intellias.intellistart.interviewplanning.model.booking.Booking;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.user.User;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

  @ManyToOne
  @JoinColumn(name = "period_id")
  private Period period;

  @OneToMany(mappedBy = "candidateSlot")
  private Set<Booking> bookings = new HashSet<>();

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
