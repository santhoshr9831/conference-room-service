package com.conference.controller;

import com.conference.dto.ConferenceRoomAvailabilityDTO;
import com.conference.dto.request.ErrorResponse;
import com.conference.exception.InputValidationException;
import com.conference.service.AvailabilityService;
import com.conference.utils.ValidationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import static com.conference.constant.Constants.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/conference-rooms")
public class AvailabilityController {

  AvailabilityService availabilityService;

  @GetMapping("/availability/{startTime}/to/{endTime}")
  @Operation(summary = "API for get conference rooms availability for time range")
  @ApiResponses(value ={
          @ApiResponse(responseCode = "200", description = "Invocation successful"),
          @ApiResponse(responseCode = "400", description = "Input or business validation error",
                  content = {
                          @Content(
                                  schema = @Schema(implementation = ErrorResponse.class)
                          )
                  })

  })
  public ResponseEntity<List<ConferenceRoomAvailabilityDTO>> getRoomsAvailability(
      @PathVariable LocalTime startTime,
      @PathVariable LocalTime endTime,
      @RequestParam(required = false) Integer locationId)
      throws InputValidationException {

    ValidationUtils.startAndEndTimeValidation(startTime, endTime);

    if (Objects.isNull(locationId)) {
      locationId = DEFAULT_LOCATION;
    }

    return ResponseEntity.ok(
        availabilityService.getConferenceRoomsAvailability(startTime.withSecond(00), endTime.withSecond(00), locationId));
  }
}
