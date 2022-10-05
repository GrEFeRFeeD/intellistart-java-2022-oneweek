package com.intellias.intellistart.interviewplanning.model.bookinglimit;

import com.intellias.intellistart.interviewplanning.model.user.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Entity for storing limit of booking per week for users with Interviewer role.
 */
@Entity
@Table(name = "booking_limits")
@Getter
@Setter
@RequiredArgsConstructor
public class BookingLimit {

  @Id
  @Column(name = "user_id")
  private Long id;

  @OneToOne
  @MapsId
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "booking_limit")
  private Integer bookingLimit;
}
