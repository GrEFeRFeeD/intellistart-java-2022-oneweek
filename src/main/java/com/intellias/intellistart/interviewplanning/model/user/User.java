package com.intellias.intellistart.interviewplanning.model.user;


import com.intellias.intellistart.interviewplanning.model.bookinglimit.BookingLimit;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * User entity.
 */
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

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  @PrimaryKeyJoinColumn
  private BookingLimit bookingLimit;

}
