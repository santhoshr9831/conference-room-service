package com.conference.service;

import com.conference.controller.request.ReservationRequest;
import com.conference.controller.request.ReservationResponse;
import com.conference.dto.ReservationDTO;
import com.conference.exception.ConferenceRoomNotAvailableException;

public interface ReservationService {
  ReservationResponse reserveConferenceRoom(ReservationDTO reservationDTO) throws ConferenceRoomNotAvailableException;
}
