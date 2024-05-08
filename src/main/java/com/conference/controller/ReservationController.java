package com.conference.controller;

import com.conference.dto.ReservationDTO;
import com.conference.dto.response.ReservationResponse;
import com.conference.dto.request.ReservationRequest;
import com.conference.dto.response.ErrorResponse;
import com.conference.exception.ConferenceRoomNotAvailableException;
import com.conference.exception.InputValidationException;
import com.conference.mapper.ReservationMapper;
import com.conference.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Objects;

@Validated
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/conference-rooms")
public class ReservationController {

  private ReservationService reservationService;

  @Operation(summary = "API for conference room reservation")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Invocation successful"),
        @ApiResponse(responseCode = "400", description = "Input or business validation error")
      })
  @PostMapping("/reservation")
  public ResponseEntity<ReservationResponse> reserveRoom(
      @Valid @RequestBody ReservationRequest request)
      throws InputValidationException, ConferenceRoomNotAvailableException {

    ReservationDTO reservationDTO = ReservationMapper.requestToDto(request);

    return ResponseEntity.ok(reservationService.reserveConferenceRoom(reservationDTO));
  }
}
