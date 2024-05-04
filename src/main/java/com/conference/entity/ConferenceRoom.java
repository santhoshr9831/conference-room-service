package com.conference.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "conference_room")
@Entity
public class ConferenceRoom {

    @Column(name = "cnf_room_id")
    @Id
    private Integer id;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "room_capacity")
    private Integer roomCapacity;

    @Column(name = "location_id")
    private Integer locationId;
}
