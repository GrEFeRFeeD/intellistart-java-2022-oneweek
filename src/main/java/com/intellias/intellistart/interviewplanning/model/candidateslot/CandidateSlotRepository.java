package com.intellias.intellistart.interviewplanning.model.candidateslot;

import com.intellias.intellistart.interviewplanning.model.user.User;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * DAO for CandidateSlot entity.
 */
public interface CandidateSlotRepository extends CrudRepository<CandidateSlot, Long> {
  List<CandidateSlot> getCandidateSlotsByUser(User user);

  List<CandidateSlot> getCandidateSlotsByUserAndDate(User user, LocalDate date);
}
