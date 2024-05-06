package com.conference.service.impl;

import com.conference.dto.ConferenceRoomAvailabilityDTO;
import com.conference.dto.ConferenceRoomDTO;
import com.conference.dto.ReservationDTO;
import com.conference.exception.ConferenceRoomNotAvailableException;
import com.conference.service.AvailabilityService;
import com.conference.service.ConferenceRoomService;
import com.conference.service.ReservationService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.conference.utils.Constants.*;

@AllArgsConstructor
@Service
@Slf4j
public class AvailabilityServiceImpl implements AvailabilityService {

  final ReservationService reservationService;

  final ConferenceRoomService conferenceRoomService;

  final ModelMapper modelMapper;

  @SneakyThrows
  @Override
  public List<ConferenceRoomAvailabilityDTO> getConferenceRoomsAvailability(
      LocalTime startTime, LocalTime endTime, Integer locationId) {

    List<ConferenceRoomDTO> roomList =
        conferenceRoomService.findConferenceRoomsByLocation(locationId);

    log.info("{} no of conference room in location {}", roomList.size(), locationId);

    if (roomList.isEmpty()) {
      log.info("Conference room not found for location {}", locationId);
      throw new ConferenceRoomNotAvailableException("Conference room not found for given location");
    }
    List<Integer> reservationsList =
        reservationService.findReservationsAndMaintenance(
            ReservationDTO.builder()
                .meetingDate(LocalDate.now())
                .startTime(startTime)
                .endTime(endTime)
                .locationId(locationId)
                .build());

    boolean isMaintenance = reservationsList.contains(MAINTENANCE);

    return roomList.stream()
        .map(
            conferenceRoom ->
                ConferenceRoomAvailabilityDTO.builder()
                    .roomName(conferenceRoom.getRoomName())
                    .capacity(conferenceRoom.getRoomCapacity())
                    .available(!isMaintenance && !reservationsList.contains(conferenceRoom.getId()))
                    .build())
        .toList();
  }
}
