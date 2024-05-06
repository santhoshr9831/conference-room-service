package com.conference.service.impl;

import com.conference.dto.ConferenceRoomDTO;
import com.conference.repository.ConferenceRoomRepository;
import com.conference.service.ConferenceRoomService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConferenceRoomServiceImpl implements ConferenceRoomService {

  ConferenceRoomRepository roomRepository;

  ModelMapper modelMapper;

  @Override
  public List<ConferenceRoomDTO> findConferenceRoomsMatchingCapacity(
      Integer capacity, Integer locationId) {

    return roomRepository.findRoomsByCapacity(capacity, locationId).stream()
        .map(
            conferenceRoom ->
                modelMapper
                    .map(conferenceRoom, ConferenceRoomDTO.ConferenceRoomDTOBuilder.class)
                    .build())
        .toList();
  }

  @Override
  public List<ConferenceRoomDTO> findConferenceRoomsByLocation(Integer locationId) {

    return roomRepository.findAllByLocationIdAndIsActive(locationId, true).stream()
        .map(
            conferenceRoom ->
                modelMapper
                    .map(conferenceRoom, ConferenceRoomDTO.ConferenceRoomDTOBuilder.class)
                    .build())
        .toList();
  }
}
