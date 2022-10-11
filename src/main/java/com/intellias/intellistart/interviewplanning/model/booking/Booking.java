package com.intellias.intellistart.interviewplanning.model.booking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlot;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Booking entity.
 */
@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

  @EmbeddedId
  private BookingKey id;

  private String subject;

  private String description;

  @ManyToOne
  @MapsId("interviewerSlotId")
  @JoinColumn(name = "interviewer_slot_id")
  private InterviewerSlot interviewerSlot;

  @ManyToOne
  @MapsId("candidateSlotId")
  @JoinColumn(name = "candidate_slot_id")
  private CandidateSlot candidateSlot;

  @ManyToOne
  @MapsId("periodId")
  @JoinColumn(name = "period_id")
  private Period period;


  @Override
  public String toString() {
    return "Booking{"
        + "id=" + id
        + ", period=" + period
        + ", subject='" + subject + '\''
        + ", description='" + description + '\''
        + ", interviewerSlot=" + interviewerSlot.getId()
        + ", candidateSlot=" + candidateSlot.getId()
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Booking booking = (Booking) o;
    return Objects.equals(id, booking.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
