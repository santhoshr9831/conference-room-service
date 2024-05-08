package com.conference.service.impl;

import com.conference.dto.response.ReservationResponse;
import com.conference.dto.ConferenceRoomDTO;
import com.conference.dto.ReservationDTO;
import com.conference.entity.Reservation;
import com.conference.exception.ConferenceRoomNotAvailableException;
import com.conference.exception.InputValidationException;
import com.conference.mapper.ReservationMapper;
import com.conference.repository.ReservationRepository;
import com.conference.service.ConferenceRoomService;
import com.conference.service.ReservationService;
import com.conference.utils.ValidationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.conference.constant.Constants.*;
import static com.conference.constant.ErrorCodes.*;

@Slf4j
@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {

  final ConferenceRoomService roomService;

  final ReservationRepository reservationRepository;

  @Override
  public ReservationResponse reserveConferenceRoom(ReservationDTO reservationDTO)
      throws ConferenceRoomNotAvailableException {

    validateInput(reservationDTO);
    log.info(INPUT_VALIDATION_SUCCESS);

    // Get list of conference room based on capacity
    List<ConferenceRoomDTO> roomList =
        roomService.getConferenceRoomsMatchingCapacity(
            reservationDTO.roomCapacity(), reservationDTO.locationId());

    if (CollectionUtils.isEmpty(roomList)) {
      log.info(
          ROOM_NOT_AVAILABLE_OR_CAPACITY_NOT_MET.getErrorMessage(),
          reservationDTO.roomCapacity(),
          reservationDTO.locationId());
      throw new ConferenceRoomNotAvailableException(
          ROOM_NOT_AVAILABLE_OR_CAPACITY_NOT_MET.getErrorCode(),
          ROOM_NOT_AVAILABLE_OR_CAPACITY_NOT_MET.getErrorMessage());
    }
    log.info(
        "Total {} no of conference room matching for location {}, noOfparticipants {}",
        roomList.size(),
        reservationDTO.locationId(),
        reservationDTO.roomCapacity());

    // Check if meeting rooms are reserved or under maintenance for given time period
    List<Integer> reservationList = findReservationsAndMaintenance(reservationDTO);

    log.info(
        "Total {} no of reservations conflicting with startTime {}, endTime {}, location {}",
        reservationList.size(),
        reservationDTO.startTime(),
        reservationDTO.endTime(),
        reservationDTO.locationId());

    if (reservationList.contains(MAINTENANCE)) {
      log.info(OVERLAPS_MAINTENANCE.getErrorMessage());
      throw new ConferenceRoomNotAvailableException(
          OVERLAPS_MAINTENANCE.getErrorCode(), OVERLAPS_MAINTENANCE.getErrorMessage());
    }

    ConferenceRoomDTO availableRoom =
        roomList.stream()
            .filter(conferenceRoom -> !reservationList.contains(conferenceRoom.id()))
            .findFirst()
            .orElseThrow(
                () -> {
                  log.info(ALL_ROOMS_RESERVED.getErrorMessage());
                  return new ConferenceRoomNotAvailableException(
                      ALL_ROOMS_RESERVED.getErrorCode(), ALL_ROOMS_RESERVED.getErrorMessage());
                });

    Reservation newReservation = ReservationMapper.dtoToEntity(reservationDTO);
    newReservation.setRoomId(availableRoom.id());
    newReservation=reservationRepository.save(newReservation);

    log.info("Meeting room : {} is reserved !!!", availableRoom.roomName());

    return new ReservationResponse(
        newReservation.getReservationId(),
        newReservation.getRoomId(),
        availableRoom.roomName(),
        availableRoom.roomCapacity(),
        availableRoom.locationId(),
        newReservation.getMeetingDate().toString(),
        newReservation.getStartTime().toString(),
        newReservation.getEndTime().toString());
  }

  @Override
  public List<Integer> findReservationsAndMaintenance(ReservationDTO reservationDTO) {
    return reservationRepository.findReservationsAndMaintenance(
        reservationDTO.meetingDate(),
        reservationDTO.startTime(),
        reservationDTO.endTime(),
        reservationDTO.locationId());
  }

  private void validateInput(ReservationDTO request) {

    if (request.roomCapacity() <= 1) {
      log.info(MIN_PARTICIPANT_NOT_MET.getErrorMessage());
      throw new InputValidationException(
              MIN_PARTICIPANT_NOT_MET.getErrorCode(), MIN_PARTICIPANT_NOT_MET.getErrorMessage());
    }
    if (!Objects.isNull(request.meetingDate()) && !request.meetingDate().equals(LocalDate.now())) {
      log.info(RESERVE_CURRENT_DATE.getErrorMessage());
      throw new InputValidationException(
              RESERVE_CURRENT_DATE.getErrorCode(), RESERVE_CURRENT_DATE.getErrorMessage());
    }
    ValidationUtils.startAndEndTimeValidation(request.startTime(), request.endTime());
  }

}
