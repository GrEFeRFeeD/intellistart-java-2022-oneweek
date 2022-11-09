package com.intellias.intellistart.interviewplanning.model.booking.validation;

import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlot;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
//@ToString
public class BookingData {
  private String subject;
  private String description;
  private InterviewerSlot interviewerSlot;
  private CandidateSlot candidateSlot;
  private Period period;

  //TODO : or inherited here?


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BookingData that = (BookingData) o;
    return Objects.equals(subject, that.subject) && Objects.equals(description,
        that.description) && Objects.equals(interviewerSlot, that.interviewerSlot)
        && Objects.equals(candidateSlot, that.candidateSlot) && Objects.equals(
        period, that.period);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subject, description, interviewerSlot, candidateSlot, period);
  }

  @Override
  public String toString() {
    return "BookingData{" +
        "subject='" + subject + '\''
        + ", description='" + description + '\''
        + ", interviewerSlot=" + interviewerSlot
        + ", candidateSlot=" + candidateSlot
        + ", period=" + period
        + '}';
  }
}
