package com.conference.service;

import com.conference.dto.ConferenceRoomDTO;

import java.util.List;

public interface ConferenceRoomService {

  List<ConferenceRoomDTO> getConferenceRoomsMatchingCapacity(Integer capacity, Integer locationId);

  List<ConferenceRoomDTO> getConferenceRoomsByLocation(Integer locationId);
}
