package com.conference.controller.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class ReservationResponse {

    private int reservationId;
    private int roomId;
    private String roomName;
    private int roomCapacity;
    private int locationId;
    private LocalDate meetingDate;
    private LocalTime startTime;
    private LocalTime endTime;
}
