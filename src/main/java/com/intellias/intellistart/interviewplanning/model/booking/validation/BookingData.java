package com.intellias.intellistart.interviewplanning.model.booking.validation;

import com.intellias.intellistart.interviewplanning.model.candidateslot.CandidateSlot;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookingData {
  private String subject;
  private String description;
  private InterviewerSlot interviewerSlot;
  private CandidateSlot candidateSlot;
  private Period period;

  //TODO : or inherited here?
}
