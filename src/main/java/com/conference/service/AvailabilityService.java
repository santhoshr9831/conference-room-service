package com.conference.service;

import com.conference.dto.ConferenceRoomAvailabilityDTO;

import java.time.LocalTime;
import java.util.List;

public interface AvailabilityService {
  List<ConferenceRoomAvailabilityDTO> getConferenceRoomsAvailability(
      LocalTime startTime, LocalTime endTime, Integer locationId);
}
