package com.intellias.intellistart.interviewplanning.model.period;

import java.time.LocalTime;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 * DAO for Period entity.
 */
public interface PeriodRepository extends CrudRepository<Period, Long> {

  Optional<Period> find(LocalTime from, LocalTime to);
}
