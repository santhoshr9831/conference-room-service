package com.conference.controller.api;

import com.conference.controller.request.ReservationResponse;
import com.conference.controller.request.ReservationRequest;
import com.conference.dto.ReservationDTO;
import com.conference.exception.InputValidationException;
import com.conference.service.ReservationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/conference-rooms")
public class ReservationController {

  ReservationService reservationService;

  @PostMapping("/reserve")
  public ResponseEntity<ReservationResponse> reserveRoom(
      @Valid @RequestBody ReservationRequest request) throws Exception {

    validateInput(request);

    return ResponseEntity.ok(
        reservationService.reserveConferenceRoom(
            ReservationDTO.builder()
                .roomCapacity(request.getNoOfParticipants())
                .meetingDate(request.getStartTime().toLocalDate())
                .startTime(request.getStartTime().toLocalTime())
                .endTime(request.getEndTime().toLocalTime())
                .locationId(request.getLocationId()==null ? 1 : request.getLocationId())
                .build()));
  }

  private void validateInput(ReservationRequest request) throws InputValidationException {
    if(request.getStartTime().equals(request.getEndTime())){
      throw new InputValidationException("Start time and end time cannot be same");
    }
  }
}