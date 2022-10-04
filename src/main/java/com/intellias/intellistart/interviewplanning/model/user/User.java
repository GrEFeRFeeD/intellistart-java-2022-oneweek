package com.intellias.intellistart.interviewplanning.model.user;


import com.intellias.intellistart.interviewplanning.model.role.Role;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@RequiredArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  private String email;

  @Enumerated(EnumType.STRING)
  private Role role;

  private Integer bookingLimit;

  // TODO: 1:N with InterviewerSlot
  // TODO: 1:N with CandidateSlot
  // TODO: 1:N with Booking
}
