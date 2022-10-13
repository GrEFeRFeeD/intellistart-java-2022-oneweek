package com.intellias.intellistart.interviewplanning.model.period.services.validation.inner;

import java.time.LocalTime;

public interface InnerValidator {

  boolean isNotCorrect(LocalTime from, LocalTime to);
}
