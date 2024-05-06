package com.conference.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConferenceRoomDTO {
  private Integer id;
  private String roomName;
  private Integer roomCapacity;
  private Integer locationId;
}
