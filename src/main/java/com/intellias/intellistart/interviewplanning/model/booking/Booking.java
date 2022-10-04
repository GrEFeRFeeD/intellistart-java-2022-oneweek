package com.intellias.intellistart.interviewplanning.model.booking;

import com.intellias.intellistart.interviewplanning.utils.LocalDateAttributeConverter;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import jdk.jfr.Timestamp;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Booking entity.
 */
@Entity
@Table(name = "bookings")
@Getter
@Setter
@RequiredArgsConstructor
public class Booking {

  @EmbeddedId
  private BookingKey id;

  /* TODO: Persist LocalTime/Date
  @Temporal(TemporalType.TIMESTAMP)
  private Date from;

  @Temporal(TemporalType.TIMESTAMP)
  private Date to;
   */

  private String subject;

  private String description;

  // TODO: N:M with InterviewerSlot
  // TODO: N:M with CandidateSlot

}
