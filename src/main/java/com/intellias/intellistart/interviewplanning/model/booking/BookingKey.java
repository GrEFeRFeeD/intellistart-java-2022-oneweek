package com.intellias.intellistart.interviewplanning.model.booking;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Embedded entity for Booking key.
 */
@Embeddable
@RequiredArgsConstructor
@Getter
@Setter
public class BookingKey implements Serializable {

  @Column(name = "candidate_slot_id")
  private Long candidateSlotId;

  @Column(name = "interviewer_slot_id")
  private Long interviewerSlotId;
}
