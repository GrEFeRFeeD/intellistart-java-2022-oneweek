package com.intellias.intellistart.interviewplanning.model.period.services.validation.chain;

import java.time.LocalTime;

/**
 * Single business logic validator
 */
public interface ChainValidator {

  boolean isNotCorrect(LocalTime from, LocalTime to);
}
