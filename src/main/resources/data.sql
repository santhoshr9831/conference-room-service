--Location insert
insert into office_location (building_name, country) values ('HQ', 'UAE');

--conference room insert
insert into conference_room (room_name,room_capacity,location_id) values ('Amaze', 3, 1 );
insert into conference_room (room_name,room_capacity,location_id) values ('Beauty', 7, 1);
insert into conference_room (room_name,room_capacity,location_id) values ('Inspire', 12, 1);
insert into conference_room (room_name,room_capacity,location_id) values ('Strive', 20, 1);

--Maintenance
insert into cnf_room_maintenance ( room_id, location_id, mnt_start_time, mnt_end_time) values ( 0, 1, '09:00:00', '09:15:00');
insert into cnf_room_maintenance ( room_id, location_id, mnt_start_time, mnt_end_time) values ( 0, 1, '13:00:00', '13:15:00');
insert into cnf_room_maintenance ( room_id, location_id, mnt_start_time, mnt_end_time) values ( 0, 1, '17:00:00', '17:15:00');