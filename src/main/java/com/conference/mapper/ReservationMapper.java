package com.conference.mapper;

import com.conference.dto.ReservationDTO;
import com.conference.dto.request.ReservationRequest;
import com.conference.entity.Reservation;

import java.time.LocalDate;
import java.util.Objects;

import static com.conference.constant.Constants.DEFAULT_LOCATION;

public class ReservationMapper {

  public static ReservationDTO requestToDto(ReservationRequest request) {
    return new ReservationDTO(
        null,
        null,
        request.noOfParticipants(),
        Objects.isNull(request.meetingDate())? LocalDate.now():request.meetingDate(),
        request.startTime().withSecond(00).withNano(000),
        request.endTime().withSecond(00).withNano(000),
        Objects.isNull(request.locationId()) ? DEFAULT_LOCATION : request.locationId());
  }

  public static Reservation dtoToEntity(ReservationDTO dto) {
    Reservation reservation = new Reservation();
    reservation.setReservationId(dto.reservationId());
    reservation.setMeetingDate(dto.meetingDate());
    reservation.setStartTime(dto.startTime());
    reservation.setEndTime(dto.endTime());
    return reservation;
  }
}
