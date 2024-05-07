package com.conference.controller;

import com.conference.dto.request.ReservationResponse;
import com.conference.dto.request.ReservationRequest;
import com.conference.dto.ReservationDTO;
import com.conference.dto.request.ErrorResponse;
import com.conference.exception.ConferenceRoomNotAvailableException;
import com.conference.exception.InputValidationException;
import com.conference.service.ReservationService;
import com.conference.utils.ValidationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Objects;

import static com.conference.constant.Constants.*;
import static com.conference.constant.ErrorCodes.MIN_PARTICIPANT_NOT_MET;
import static com.conference.constant.ErrorCodes.RESERVE_CURRENT_DATE;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/conference-rooms")
public class ReservationController {

  private ReservationService reservationService;

  @PostMapping("/reservation")
  @Operation(summary = "API for conference room reservation")
  @ApiResponses(value ={
          @ApiResponse(responseCode = "200", description = "Invocation successful"),
          @ApiResponse(responseCode = "400", description = "Input or business validation error",
                  content = {
                          @Content(
                                  mediaType = "application/json",
                                  schema = @Schema(implementation = ErrorResponse.class)
                          )
                  })

  })
  public ResponseEntity<ReservationResponse> reserveRoom(
      @Valid @RequestBody ReservationRequest request)
      throws InputValidationException, ConferenceRoomNotAvailableException {

    validateInput(request);

    return ResponseEntity.ok(
        reservationService.reserveConferenceRoom(
            ReservationDTO.builder()
                .roomCapacity(request.getNoOfParticipants())
                .meetingDate(request.getStartTime().toLocalDate())
                .startTime(request.getStartTime().toLocalTime().withSecond(00))
                .endTime(request.getEndTime().toLocalTime().withSecond(00))
                .locationId(
                    Objects.isNull(request.getLocationId())
                        ? DEFAULT_LOCATION
                        : request.getLocationId())
                .build()));
  }

  private void validateInput(ReservationRequest request) throws InputValidationException {

    if (request.getNoOfParticipants() <= 1) {
      log.info(MIN_PARTICIPANT_NOT_MET.getErrorMessage());
      throw new InputValidationException(MIN_PARTICIPANT_NOT_MET.getErrorCode(),MIN_PARTICIPANT_NOT_MET.getErrorMessage());
    }
    if (!request.getStartTime().toLocalDate().equals(LocalDate.now())
        || !request.getEndTime().toLocalDate().equals(LocalDate.now())) {
      log.info(RESERVE_CURRENT_DATE.getErrorMessage());
      throw new InputValidationException(RESERVE_CURRENT_DATE.getErrorCode(),RESERVE_CURRENT_DATE.getErrorMessage());
    }

    ValidationUtils.startAndEndTimeValidation(
        request.getStartTime().toLocalTime(), request.getEndTime().toLocalTime());
  }
}
