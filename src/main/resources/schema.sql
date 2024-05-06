create table office_location(location_id tinyint primary key auto_increment,
building_name varchar(100) not null,
country varchar(50) not null,
is_active bit default 1
);


create table conference_room(cnf_room_id tinyint primary key auto_increment,
room_name varchar(100) not null,
room_capacity tinyint not null,
location_id tinyint not null,
is_active bit default 1,
foreign key (location_id) references office_location(location_id)
);

create table cnf_room_maintenance(maintenance_id int primary key auto_increment,
room_id tinyint,
location_id tinyint not null,
mnt_start_time time not null,
mnt_end_time time not null,
is_active bit default 1,
foreign key (location_id) references office_location(location_id)
);


create table cnf_room_reservations(reservation_id int primary key auto_increment,
room_id tinyint,
meeting_date date not null,
start_time time not null,
end_time time not null,
is_active bit default 1,
foreign key (room_id) references conference_room(cnf_room_id)
);
