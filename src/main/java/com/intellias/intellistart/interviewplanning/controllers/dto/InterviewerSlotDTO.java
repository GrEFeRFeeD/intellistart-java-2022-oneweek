package com.intellias.intellistart.interviewplanning.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class InterviewerSlotDTO {
  private Long interviewerId;
  private Long week;
  @NonNull
  private String dayOfWeek;
  @NonNull
  private String from;
  @NonNull
  private String to;

}