package com.conference.controller;

import com.conference.dto.ConferenceRoomAvailabilityDTO;
import com.conference.exception.InputValidationException;
import com.conference.service.AvailabilityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AvailabilityControllerTest {

    @InjectMocks
    AvailabilityController availabilityController;
    @Mock
    AvailabilityService availabilityService;


    public List<ConferenceRoomAvailabilityDTO> getMeetingRoom(){
        return List.of(new ConferenceRoomAvailabilityDTO("room1",2,true),
                new ConferenceRoomAvailabilityDTO("room2",5,false));
    }
    public List<ConferenceRoomAvailabilityDTO> getMeetingRoomWithNoAvailability(){
        return List.of(new ConferenceRoomAvailabilityDTO("room1",2,false),
                new ConferenceRoomAvailabilityDTO("room2",5,false));
    }

    @Test
    public void test_list_of_conferenceroom() throws InputValidationException {
        // Given
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(10, 0);
        when(availabilityService.getConferenceRoomsAvailability(LocalTime.of(9,0),endTime,1))
                .thenReturn(getMeetingRoom());

        // When
        ResponseEntity<List<ConferenceRoomAvailabilityDTO>> response = availabilityController.getRoomsAvailability(startTime, endTime,1);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<ConferenceRoomAvailabilityDTO> availabilityList = response.getBody();
        assertNotNull(availabilityList);
    }

    @Test
    public void test_1st_room_available_2nd_room_not_available() throws InputValidationException {
        // Given
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(10, 0);
        when(availabilityService.getConferenceRoomsAvailability(LocalTime.of(9,0),endTime,1))
                .thenReturn(getMeetingRoom());

        // When
        ResponseEntity<List<ConferenceRoomAvailabilityDTO>> result = availabilityController.getRoomsAvailability(startTime, endTime,1);

        // Then
        assertEquals(2, result.getBody().size());
        assertTrue(result.getBody().get(0).available());
        assertFalse(result.getBody().get(1).available());

    }

    @Test
    public void test_all_room_not_available() throws InputValidationException {
        // Given
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(10, 0);
        when(availabilityService.getConferenceRoomsAvailability(LocalTime.of(9,0),endTime,1))
                .thenReturn(getMeetingRoomWithNoAvailability());

        // When
        ResponseEntity<List<ConferenceRoomAvailabilityDTO>> result = availabilityController.getRoomsAvailability(startTime, endTime,1);

        // Then
        assertEquals(2, result.getBody().size());
        for (ConferenceRoomAvailabilityDTO dto : result.getBody()) {
            assertFalse(dto.available());
        }
    }

}
