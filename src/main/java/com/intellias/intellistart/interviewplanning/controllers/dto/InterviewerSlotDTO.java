package com.intellias.intellistart.interviewplanning.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class InterviewerSlotDTO {
  private Long interviewerId;
  private Long week;
  private String dayOfWeek;
  private String from;
  private String to;

}
