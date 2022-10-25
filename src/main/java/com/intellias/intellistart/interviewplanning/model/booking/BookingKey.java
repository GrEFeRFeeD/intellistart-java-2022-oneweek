package com.intellias.intellistart.interviewplanning.model.booking;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Embedded entity (complex PK) for Booking entity.
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

  @Column(name = "period_id")
  private Long periodId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BookingKey that = (BookingKey) o;
    return Objects.equals(candidateSlotId, that.candidateSlotId)
        && Objects.equals(interviewerSlotId, that.interviewerSlotId)
        && Objects.equals(periodId, that.periodId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(candidateSlotId, interviewerSlotId, periodId);
  }
}