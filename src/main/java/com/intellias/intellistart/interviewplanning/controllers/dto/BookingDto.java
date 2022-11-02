package com.intellias.intellistart.interviewplanning.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingDto {

    private Long interviewerSlotId;
    private Long candidateSlotId;
    private String from;
    private String to;
    private String subject;
    private String description;

    @Override
    public String toString() {
        return "BookingDto{" +
                "interviewerSlotId=" + interviewerSlotId +
                ", candidateSlotId=" + candidateSlotId +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
