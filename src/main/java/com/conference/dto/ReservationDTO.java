package com.conference.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationDTO(
    Integer reservationId, Integer roomId, Integer roomCapacity, LocalDate meetingDate,
    LocalTime startTime, LocalTime endTime, Integer locationId) {
  public ReservationDTO(
      LocalDate meetingDate, LocalTime startTime, LocalTime endTime, Integer locationId) {
    this(null, null, null, meetingDate, startTime, endTime, locationId);
  }
}
