package com.conference.service.impl;

import com.conference.dto.ConferenceRoomDTO;
import com.conference.mapper.ConferenceRoomMapper;
import com.conference.repository.ConferenceRoomRepository;
import com.conference.service.ConferenceRoomService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConferenceRoomServiceImpl implements ConferenceRoomService {

  ConferenceRoomRepository roomRepository;

  @Override
  public List<ConferenceRoomDTO> getConferenceRoomsMatchingCapacity(
      Integer capacity, Integer locationId) {

    return roomRepository.findRoomsByCapacity(capacity, locationId).stream()
        .map(conferenceRoom -> ConferenceRoomMapper.entityToDTO(conferenceRoom))
        .toList();
  }

  @Override
  public List<ConferenceRoomDTO> getConferenceRoomsByLocation(Integer locationId) {

    return roomRepository.findAllByLocationIdAndIsActive(locationId, true).stream()
        .map(conferenceRoom -> ConferenceRoomMapper.entityToDTO(conferenceRoom))
        .toList();
  }
}
