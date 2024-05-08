package com.conference.service.impl;

import com.conference.dto.ConferenceRoomAvailabilityDTO;
import com.conference.dto.ConferenceRoomDTO;
import com.conference.dto.ReservationDTO;
import com.conference.exception.ConferenceRoomNotAvailableException;
import com.conference.service.AvailabilityService;
import com.conference.service.ConferenceRoomService;
import com.conference.service.ReservationService;
import com.conference.utils.ValidationUtils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.conference.constant.Constants.*;
import static com.conference.constant.ErrorCodes.ROOM_NOT_AVAILABLE_FOR_LOCATION;

@AllArgsConstructor
@Service
@Slf4j
public class AvailabilityServiceImpl implements AvailabilityService {

  final ReservationService reservationService;

  final ConferenceRoomService conferenceRoomService;

  @SneakyThrows
  @Override
  public List<ConferenceRoomAvailabilityDTO> getConferenceRoomsAvailability(
      LocalTime startTime, LocalTime endTime, Integer locationId) {

    ValidationUtils.startAndEndTimeValidation(startTime, endTime);

    log.info(INPUT_VALIDATION_SUCCESS);


    List<ConferenceRoomDTO> roomList =
        conferenceRoomService.getConferenceRoomsByLocation(locationId);

    log.info("{} no of conference room in location {}", roomList.size(), locationId);

    if (roomList.isEmpty()) {
      log.info("Conference room not found for location {}", locationId);
      throw new ConferenceRoomNotAvailableException(
          ROOM_NOT_AVAILABLE_FOR_LOCATION.getErrorCode(),
          ROOM_NOT_AVAILABLE_FOR_LOCATION.getErrorMessage());
    }
    List<Integer> reservationsList =
        reservationService.findReservationsAndMaintenance(
            new ReservationDTO(LocalDate.now(), startTime, endTime, locationId));

    boolean isMaintenance = reservationsList.contains(MAINTENANCE);

    return roomList.stream()
        .map(
            conferenceRoom ->
                new ConferenceRoomAvailabilityDTO(
                    conferenceRoom.roomName(),
                    conferenceRoom.roomCapacity(),
                    !isMaintenance && !reservationsList.contains(conferenceRoom.id())))
        .toList();
  }
}
