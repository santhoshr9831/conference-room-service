package com.conference.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConferenceRoomAvailabilityDTO {
  private String roomName;
  private Integer capacity;
  private Boolean available;
}
