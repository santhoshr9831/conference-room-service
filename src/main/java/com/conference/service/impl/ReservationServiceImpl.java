package com.conference.service.impl;

import com.conference.controller.request.ReservationResponse;
import com.conference.dto.ReservationDTO;
import com.conference.entity.ConferenceRoom;
import com.conference.entity.Reservation;
import com.conference.exception.ConferenceRoomNotAvailableException;
import com.conference.repository.ConferenceRoomRepository;
import com.conference.repository.ReservationRepository;
import com.conference.service.ReservationService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.conference.utils.Constants.MAINTENANCE;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {

  ConferenceRoomRepository roomRepository;

  ReservationRepository reservationRepository;

  ModelMapper modelMapper;

  @Override
  public ReservationResponse reserveConferenceRoom(ReservationDTO reservationDTO)
      throws ConferenceRoomNotAvailableException {

    // Get list of conference room based on capacity
    List<ConferenceRoom> roomList =
        roomRepository.findRoomsByCapacity(reservationDTO.getRoomCapacity(),reservationDTO.getLocationId());

    if (CollectionUtils.isEmpty(roomList)) {
      throw new ConferenceRoomNotAvailableException(
          "Conference room not available with expected capacity");
    }

    // Check if meeting rooms are reserved or under maintenance for given time period
    List<Integer> reservation =
        reservationRepository.findReservationsAndMaintenance(
            reservationDTO.getMeetingDate(),
            reservationDTO.getStartTime(),
            reservationDTO.getEndTime(),
            reservationDTO.getLocationId());

    if (reservation.contains(MAINTENANCE)) {
      throw new ConferenceRoomNotAvailableException(
          "Conference room is under maintenance for given time period");
    }

    ConferenceRoom availableRoom =
        roomList.stream()
            .filter(conferenceRoom -> !reservation.contains(conferenceRoom.getId()))
            .findFirst()
            .orElseThrow(
                () ->
                    new ConferenceRoomNotAvailableException(
                        "Conference room not available for given time period"));

    Reservation newReservation = modelMapper.map(reservationDTO,Reservation.class);
    newReservation.setRoomId(availableRoom.getId());
    reservationRepository.save(newReservation);

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

}
