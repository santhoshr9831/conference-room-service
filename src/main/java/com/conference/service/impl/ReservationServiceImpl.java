package com.conference.service.impl;

import com.conference.dto.request.ReservationResponse;
import com.conference.dto.ConferenceRoomDTO;
import com.conference.dto.ReservationDTO;
import com.conference.entity.Reservation;
import com.conference.exception.ConferenceRoomNotAvailableException;
import com.conference.repository.ReservationRepository;
import com.conference.service.ConferenceRoomService;
import com.conference.service.ReservationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.conference.constant.Constants.*;
import static com.conference.constant.ErrorCodes.*;

@Slf4j
@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {

  final ConferenceRoomService roomService;

  final ReservationRepository reservationRepository;

  final ModelMapper modelMapper;

  @Override
  public ReservationResponse reserveConferenceRoom(ReservationDTO reservationDTO)
      throws ConferenceRoomNotAvailableException {

    // Get list of conference room based on capacity
    List<ConferenceRoomDTO> roomList =
        roomService.findConferenceRoomsMatchingCapacity(
            reservationDTO.getRoomCapacity(), reservationDTO.getLocationId());

    if (CollectionUtils.isEmpty(roomList)) {
      log.info(
          ROOM_NOT_AVAILABLE_OR_CAPACITY_NOT_MET.getErrorMessage(),
          reservationDTO.getRoomCapacity(),
          reservationDTO.getLocationId());
      throw new ConferenceRoomNotAvailableException(
          ROOM_NOT_AVAILABLE_OR_CAPACITY_NOT_MET.getErrorCode(),
          ROOM_NOT_AVAILABLE_OR_CAPACITY_NOT_MET.getErrorMessage());
    }
    log.info(
        "Total {} no of conference room matching for location {}, noOfparticipants {}",
        roomList.size(),
        reservationDTO.getLocationId(),
        reservationDTO.getRoomCapacity());

    // Check if meeting rooms are reserved or under maintenance for given time period
    List<Integer> reservationList = findReservationsAndMaintenance(reservationDTO);

    log.info(
        "Total {} no of reservations conflicting with startTime {}, endTime {}, location {}",
        reservationList.size(),
        reservationDTO.getStartTime(),
        reservationDTO.getEndTime(),
        reservationDTO.getLocationId());

    if (reservationList.contains(MAINTENANCE)) {
      log.info(OVERLAPS_MAINTENANCE.getErrorMessage());
      throw new ConferenceRoomNotAvailableException(
          OVERLAPS_MAINTENANCE.getErrorCode(), OVERLAPS_MAINTENANCE.getErrorMessage());
    }

    ConferenceRoomDTO availableRoom =
        roomList.stream()
            .filter(conferenceRoom -> !reservationList.contains(conferenceRoom.getId()))
            .findFirst()
            .orElseThrow(
                () -> {
                  log.info(ALL_ROOMS_RESERVED.getErrorMessage());
                  return new ConferenceRoomNotAvailableException(
                      ALL_ROOMS_RESERVED.getErrorCode(), ALL_ROOMS_RESERVED.getErrorMessage());
                });

    Reservation newReservation = modelMapper.map(reservationDTO, Reservation.class);
    newReservation.setRoomId(availableRoom.getId());
    reservationRepository.save(newReservation);

    log.info("Meeting room : {} is reserved !!!", availableRoom.getRoomName());

    return ReservationResponse.builder()
        .reservationId(newReservation.getReservationId())
        .roomName(availableRoom.getRoomName())
        .roomId(newReservation.getRoomId())
        .roomCapacity(availableRoom.getRoomCapacity())
        .locationId(availableRoom.getLocationId())
        .meetingDate(newReservation.getMeetingDate().toString())
        .startTime(newReservation.getStartTime().toString())
        .endTime(newReservation.getEndTime().toString())
        .build();
  }

  @Override
  public List<Integer> findReservationsAndMaintenance(ReservationDTO reservationDTO) {
    return reservationRepository.findReservationsAndMaintenance(
        reservationDTO.getMeetingDate(),
        reservationDTO.getStartTime(),
        reservationDTO.getEndTime(),
        reservationDTO.getLocationId());
  }
}
