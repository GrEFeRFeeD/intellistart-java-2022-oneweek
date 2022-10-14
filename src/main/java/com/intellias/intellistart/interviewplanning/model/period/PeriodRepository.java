package com.intellias.intellistart.interviewplanning.model.period;

import java.time.LocalTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * DAO for Period entity.
 */
public interface PeriodRepository extends CrudRepository<Period, Long> {

  @Query("select p from Period p where p.from = ?1 and p.to = ?2")
  Optional<Period> findPeriod(LocalTime from, LocalTime to);
}
