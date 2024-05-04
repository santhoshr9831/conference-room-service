package com.conference.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationRequest {
  private Integer noOfParticipants;

  @NotNull(message = "StartTime cannot be null")
  private LocalDateTime startTime;

  @NotNull(message = "EndTime cannot be null")
  private LocalDateTime endTime;

  private Integer locationId;
}
