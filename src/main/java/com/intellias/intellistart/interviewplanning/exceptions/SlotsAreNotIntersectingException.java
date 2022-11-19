package com.intellias.intellistart.interviewplanning.exceptions;

/**
 * Is thrown in obtaining periods when parameters are invalid.
 * <ul>
 * <li>periods of InterviewSlot, CandidateSlot do not intersect with new Period
 * <li>new Period is overlapping with existing Periods of InterviewerSlot and CandidateSlot
 * </ul>
 */
public class SlotsAreNotIntersectingException extends IllegalArgumentException {
}
