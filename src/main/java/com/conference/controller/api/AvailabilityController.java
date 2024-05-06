package com.conference.controller.api;

import com.conference.dto.ConferenceRoomAvailabilityDTO;
import com.conference.exception.InputValidationException;
import com.conference.service.AvailabilityService;
import com.conference.utils.ValidationUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import static com.conference.utils.Constants.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/conference-rooms")
public class AvailabilityController {

  AvailabilityService availabilityService;

  @GetMapping("/availability/{startTime}/to/{endTime}")
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
        availabilityService.getConferenceRoomsAvailability(startTime, endTime, locationId));
  }
}
