package com.conference.service.impl;

import com.conference.controller.request.ReservationResponse;
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

import static com.conference.utils.Constants.MAINTENANCE;

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
          "Conference room not available for capacity {}, location {}",
          reservationDTO.getRoomCapacity(),
          reservationDTO.getLocationId());
      throw new ConferenceRoomNotAvailableException(
          "Conference room not available with expected capacity");
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
      log.info("Reservation overlaps maintenance window");
      throw new ConferenceRoomNotAvailableException("Reservation overlaps maintenance window");
    }

    ConferenceRoomDTO availableRoom =
        roomList.stream()
            .filter(conferenceRoom -> !reservationList.contains(conferenceRoom.getId()))
            .findFirst()
            .orElseThrow(
                () -> {
                  log.info(
                      "Conference room not available for given time period. Check availability before reservation");
                  return new ConferenceRoomNotAvailableException(
                      "Conference room not available for given time period. Check availability before reservation");
                });

    Reservation newReservation = modelMapper.map(reservationDTO, Reservation.class);
    newReservation.setRoomId(availableRoom.getId());
    reservationRepository.save(newReservation);

    log.info("Meeting room : {} is reserved !!", availableRoom.getRoomName());

    return ReservationResponse.builder()
        .reservationId(newReservation.getReservationId())
        .roomName(availableRoom.getRoomName())
        .roomId(newReservation.getRoomId())
        .roomCapacity(availableRoom.getRoomCapacity())
        .locationId(availableRoom.getLocationId())
        .meetingDate(newReservation.getMeetingDate())
        .startTime(newReservation.getStartTime())
        .endTime(newReservation.getEndTime())
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
