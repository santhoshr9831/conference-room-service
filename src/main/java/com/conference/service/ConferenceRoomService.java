package com.conference.service;

import com.conference.dto.ConferenceRoomDTO;

import java.util.List;

public interface ConferenceRoomService {

  List<ConferenceRoomDTO> findConferenceRoomsMatchingCapacity(Integer capacity, Integer locationId);

  List<ConferenceRoomDTO> findConferenceRoomsByLocation(Integer locationId);
}
