package com.intellias.intellistart.interviewplanning.model.interviewerslot;

import com.intellias.intellistart.interviewplanning.model.booking.Booking;
import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.week.Week;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * InterviewerSlot entity.
 */
@Entity
@Table(name = "interviewer_slots")
@Getter
@Setter
@RequiredArgsConstructor
public class InterviewerSlot {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "interviewer_slot_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "week_id")
  private Week week;

  @Enumerated
  private DayOfWeek dayOfWeek;

  @ManyToOne
  @JoinColumn(name = "period_id")
  private Period period;

  @OneToMany(mappedBy = "interviewerSlot")
  private Set<Booking> bookings = new HashSet<>();

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
