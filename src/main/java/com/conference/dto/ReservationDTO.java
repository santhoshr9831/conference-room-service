package com.conference.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class ReservationDTO {
  private Integer reservationId;
  private Integer roomId;
  private Integer roomCapacity;
  private LocalDate meetingDate;
  private LocalTime startTime;
  private LocalTime endTime;
  private Integer locationId;
}
