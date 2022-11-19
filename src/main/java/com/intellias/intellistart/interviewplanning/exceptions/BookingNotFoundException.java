package com.intellias.intellistart.interviewplanning.exceptions;

import com.intellias.intellistart.interviewplanning.model.booking.BookingService;

/**
 * Is thrown in {@link BookingService#findById(Long)}.
 * When parameters are invalid: can't be read as time or wrong business logic.
 */
public class BookingNotFoundException extends IllegalArgumentException {
}
