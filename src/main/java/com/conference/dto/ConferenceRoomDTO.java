package com.conference.dto;

import com.conference.entity.ConferenceRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class ConferenceRoomDTO {
  private Integer id;
  private String roomName;
  private Integer roomCapacity;
  private Integer locationId;
}
