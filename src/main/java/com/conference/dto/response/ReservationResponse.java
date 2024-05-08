package com.conference.dto.response;

public record ReservationResponse(int reservationId, int roomId, String roomName, int roomCapacity, int locationId,
    String meetingDate, String startTime, String endTime) {}
