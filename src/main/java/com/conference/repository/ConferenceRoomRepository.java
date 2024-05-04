package com.conference.repository;

import com.conference.entity.ConferenceRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConferenceRoomRepository extends JpaRepository<ConferenceRoom, Integer> {

    @Query("SELECT r from ConferenceRoom r WHERE r.roomCapacity >=:roomCapacity and r.locationId=:locationId order by r.roomCapacity")
    List<ConferenceRoom> findRoomsByCapacity(Integer roomCapacity, int locationId);

}
