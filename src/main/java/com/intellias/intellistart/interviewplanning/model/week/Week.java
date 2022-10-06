package com.intellias.intellistart.interviewplanning.model.week;

import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity for week.
 */
@Entity
@Table(name = "weeks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Week {

  @Id
  @Column(name = "week_id")
  private Long id;

  @OneToMany(mappedBy = "week")
  private Set<InterviewerSlot> interviewerSlots = new HashSet<>();

  public void addInterviewerSlot(InterviewerSlot interviewerSlot) {
    interviewerSlots.add(interviewerSlot);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Week week = (Week) o;
    return Objects.equals(id, week.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
