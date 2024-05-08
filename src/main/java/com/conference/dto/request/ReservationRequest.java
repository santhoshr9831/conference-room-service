package com.conference.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "Tutorial Model Information")
public record ReservationRequest(
    @Schema(description = "No of participant joining the meeting", example = "10", type = "integer")
        @NotNull
        Integer noOfParticipants,
    @Schema(description = "Meeting date defualt to current date", type = "Date")
        LocalDate meetingDate,
    @Schema(description = "Meeting start date", example = "10:00", type = "string")
        @NotNull(message = "Start time must not be empty")
        LocalTime startTime,
    @Schema(description = "Meeting end date", example = "11:00", type = "string")
        @NotNull(message = "End time must not be empty")
        LocalTime endTime,
    @Schema(description = "Location id default to 1", example = "1", type = "integer")
        Integer locationId) {}
