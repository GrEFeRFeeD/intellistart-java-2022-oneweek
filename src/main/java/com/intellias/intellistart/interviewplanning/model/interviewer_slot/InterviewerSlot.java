package com.intellias.intellistart.interviewplanning.model.interviewer_slot;

import com.intellias.intellistart.interviewplanning.model.day_of_week.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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

  private LocalTime from;

  private LocalTime to;

  // TODO: M:N with Booking
}
