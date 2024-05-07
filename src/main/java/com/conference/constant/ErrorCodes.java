package com.conference.constant;

import lombok.Getter;

@Getter
public enum ErrorCodes {
  INPUT_VALIDATION_FAILURE("G_0001", "Input validation failure"),
  GENERIC_EXCEPTION("G_0002", "Generic exception"),
  RESOURCE_NOT_FOUND_EXCEPTION("G_0003", "Resource not found"),
  INTERNAL_SERVER_ERROR("G_0004", "Internal server error"),
  START_EQ_END("B_0001", "Start time cannot be equal or after end time"),
  START_OR_END_15_MIN_INTERVAL("B_0002", "Start or end time should be in interval of 15 minutes"),
  MIN_PARTICIPANT_NOT_MET("B_0003", "Minimum number of participants not met"),
  RESERVE_CURRENT_DATE("B_0004", "Reservation allowed only for current date"),
  ROOM_NOT_AVAILABLE_FOR_LOCATION("B_0005", "Conference room not found for given location"),
  ROOM_NOT_AVAILABLE_OR_CAPACITY_NOT_MET(
      "B_0006", "Conference room not available for this location or for this capacity"),
  OVERLAPS_MAINTENANCE("B_0007", "Reservation overlaps maintenance window"),
  ALL_ROOMS_RESERVED(
      "B_0008",
      "All conference room reserved for given time period. Check availability before reservation");

  private final String errorCode;
  private final String errorMessage;

  ErrorCodes(String errorCode, String errorMessage) {
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }
}
