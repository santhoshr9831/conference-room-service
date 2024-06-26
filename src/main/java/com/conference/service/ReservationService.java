package com.conference.service;

import com.conference.dto.response.ReservationResponse;
import com.conference.dto.ReservationDTO;
import com.conference.exception.ConferenceRoomNotAvailableException;

import java.util.List;

public interface ReservationService {
  ReservationResponse reserveConferenceRoom(ReservationDTO reservationDTO)
      throws ConferenceRoomNotAvailableException;

  List<Integer> findReservationsAndMaintenance(ReservationDTO reservationDTO);
}
