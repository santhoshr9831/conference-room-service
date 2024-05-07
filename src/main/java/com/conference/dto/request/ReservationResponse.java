package com.conference.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class ReservationResponse {

  private int reservationId;
  private int roomId;
  private String roomName;
  private int roomCapacity;
  private int locationId;
  private String meetingDate;
  private String startTime;
  private String endTime;
}
