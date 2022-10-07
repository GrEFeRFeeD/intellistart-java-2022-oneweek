package com.intellias.intellistart.interviewplanning.model.booking;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Embedded entity for Booking key.
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookingKey implements Serializable {

  @Column(name = "candidate_slot_id")
  private Long candidateSlotId;

  @Column(name = "interviewer_slot_id")
  private Long interviewerSlotId;
}
