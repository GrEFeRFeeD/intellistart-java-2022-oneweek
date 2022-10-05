package com.intellias.intellistart.interviewplanning.model.week;

import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Entity for week.
 */
@Entity
@Table(name = "weeks")
@Getter
@Setter
@RequiredArgsConstructor
public class Week {

  @Id
  @Column(name = "week_id")
  private Long id;

  @OneToMany(mappedBy = "week")
  private Set<InterviewerSlot> interviewerSlots = new HashSet<>();

}
