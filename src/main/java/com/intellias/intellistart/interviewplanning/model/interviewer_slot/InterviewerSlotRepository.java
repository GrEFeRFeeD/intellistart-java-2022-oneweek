package com.intellias.intellistart.interviewplanning.model.interviewer_slot;

import javax.persistence.criteria.CriteriaBuilder.In;
import org.springframework.data.repository.CrudRepository;

public interface InterviewerSlotRepository extends CrudRepository<InterviewerSlot, Long> {

}
