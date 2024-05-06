package com.conference.controller.api;

import com.conference.controller.request.ReservationResponse;
import com.conference.controller.request.ReservationRequest;
import com.conference.dto.ReservationDTO;
import com.conference.exception.ConferenceRoomNotAvailableException;
import com.conference.exception.InputValidationException;
import com.conference.service.ReservationService;
import com.conference.utils.ValidationUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Objects;

import static com.conference.utils.Constants.DEFAULT_LOCATION;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/conference-rooms")
public class ReservationController {

  private ReservationService reservationService;

  @PostMapping("/reserve")
  public ResponseEntity<ReservationResponse> reserveRoom(
      @Valid @RequestBody ReservationRequest request)
      throws InputValidationException, ConferenceRoomNotAvailableException {

    validateInput(request);

    return ResponseEntity.ok(
        reservationService.reserveConferenceRoom(
            ReservationDTO.builder()
                .roomCapacity(request.getNoOfParticipants())
                .meetingDate(request.getStartTime().toLocalDate())
                .startTime(request.getStartTime().toLocalTime())
                .endTime(request.getEndTime().toLocalTime())
                .locationId(
                    Objects.isNull(request.getLocationId())
                        ? DEFAULT_LOCATION
                        : request.getLocationId())
                .build()));
  }

  private void validateInput(ReservationRequest request) throws InputValidationException {

    if (request.getNoOfParticipants() == 1) {
      throw new InputValidationException("Minimum number of participants not met");
    }
    if (!request.getStartTime().toLocalDate().equals(LocalDate.now())
        || !request.getEndTime().toLocalDate().equals(LocalDate.now())) {
      throw new InputValidationException("Reservation allowed only for current date");
    }

    ValidationUtils.startAndEndTimeValidation(
        request.getStartTime().toLocalTime(), request.getEndTime().toLocalTime());
  }
}
