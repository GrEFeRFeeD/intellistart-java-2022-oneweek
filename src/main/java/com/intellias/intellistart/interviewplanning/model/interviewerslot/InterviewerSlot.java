package com.intellias.intellistart.interviewplanning.model.interviewerslot;

import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import java.time.LocalTime;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
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

  private Integer weekNum;

  @Enumerated
  private DayOfWeek dayOfWeek;

  /* TODO: Persist LocalTime/Date
  @Temporal(TemporalType.TIMESTAMP)
  private Date from;

  @Temporal(TemporalType.TIMESTAMP)
  private Date to;
   */

  // TODO: M:N with Booking
}
