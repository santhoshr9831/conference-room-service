package com.conference.dto;

public record ConferenceRoomAvailabilityDTO(
    String roomName, Integer roomCapacity, Boolean available) {}
