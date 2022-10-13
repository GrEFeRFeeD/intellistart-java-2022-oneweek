package com.intellias.intellistart.interviewplanning.model.candidateslot;

import com.intellias.intellistart.interviewplanning.controllers.dto.CandidateSlotDto;
import com.intellias.intellistart.interviewplanning.exeptions.InvalidBoundariesException;
import com.intellias.intellistart.interviewplanning.model.user.Role;
import com.intellias.intellistart.interviewplanning.model.user.UserService;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidateSlotDtoValidator {
  private final UserService userService;

  @Autowired
  public CandidateSlotDtoValidator(UserService userService) {
    this.userService = userService;
  }

  public void validateCandidateSlotDto(CandidateSlotDto candidateSlotDto) throws InvalidBoundariesException {
    if (LocalDate.now().isAfter(candidateSlotDto.getDate()) &&
        userService.getCurrentUser().getRole() != Role.CANDIDATE) {
      //TODO: Validate Period: minimum duration of an hour and a half
      throw new InvalidBoundariesException();
    }
  }
}
