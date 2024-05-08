package com.conference.mapper;

import com.conference.dto.ConferenceRoomDTO;
import com.conference.entity.ConferenceRoom;

public class ConferenceRoomMapper {

    public static ConferenceRoomDTO entityToDTO(ConferenceRoom entity) {
        return new ConferenceRoomDTO(entity.getId(), entity.getRoomName(),entity.getRoomCapacity(),entity.getLocationId());
    }
}
