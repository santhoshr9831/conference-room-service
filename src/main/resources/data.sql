--Location insert
insert into office_location (building_name, country) values ('HQ', 'UAE');
insert into office_location (building_name, country) values ('Branch 1', 'UAE');

--conference room insert
insert into conference_room (room_name,room_capacity,location_id,is_active) values ('Amaze', 3, 1 , 1);
insert into conference_room (room_name,room_capacity,location_id,is_active) values ('Beauty', 7, 1, 1);
insert into conference_room (room_name,room_capacity,location_id,is_active) values ('Inspire', 12, 1, 1);
insert into conference_room (room_name,room_capacity,location_id,is_active) values ('Strive', 20, 1, 1);
insert into conference_room (room_name,room_capacity,location_id,is_active) values ('Edge', 15, 2, 1);
insert into conference_room (room_name,room_capacity,location_id,is_active) values ('Square', 20, 2, 1);
insert into conference_room (room_name,room_capacity,location_id,is_active) values ('Circle', 20, 2, 0);

--Maintenance
insert into cnf_room_maintenance ( room_id, location_id, mnt_start_time, mnt_end_time) values ( 0, 1, '09:00', '09:15');
insert into cnf_room_maintenance ( room_id, location_id, mnt_start_time, mnt_end_time) values ( 0, 1, '13:00', '13:15');
insert into cnf_room_maintenance ( room_id, location_id, mnt_start_time, mnt_end_time) values ( 0, 1, '17:00', '17:15');
insert into cnf_room_maintenance ( room_id, location_id, mnt_start_time, mnt_end_time) values ( 1, 1, '11:00', '11:15');
insert into cnf_room_maintenance ( room_id, location_id, mnt_start_time, mnt_end_time) values ( 0, 2, '08:00', '08:15');
insert into cnf_room_maintenance ( room_id, location_id, mnt_start_time, mnt_end_time) values ( 6, 2, '12:00', '15:15');