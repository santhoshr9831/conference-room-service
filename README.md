# conference-room-service

This is a sample Java SpringBoot microservice for the conference room booking.
It has two Rest APIs for following action.
* Conference room reservation
* Conference room availability

# Requirements

For building and running the application you need:

- JDK 17
- Git ( if you want to checkout the repository in local)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. 

One way is to execute the `main` method in the `com.conference.ConferenceRoomServiceApplication` class from your IDE.

Alternatively you can use the below command to run the application from command prompt.

Use project maven wrapper to run the application to avoid maven installation in local.
```shell
cd <Project directory>
mvnw spring-boot:run
```

Application should be up and running in port 8080 with below messages

```agsl
Tomcat started on port 8080 (http) with context path
Started ConferenceRoomServiceApplication in 4.067 seconds (process running for 4.606)
```

Spring Doc is integrated in application hence you look at API documentation as well as test API directly from Swagger

SwaggerURL: http://localhost:8080/swagger-ui/index.html


Application will be connected to H2 database for testing purposes
Schema and sample data created during application startup.

H2 console url : http://localhost:8080/h2
```agsl
Connection properties.
Driver class: org.h2.Driver
JDBC url: jdbc:h2:mem:conferencedb
Username : sa
password : password
```


Quick details about Data:
- We have two location 1, 2.
- Few meeting rooms created for each location. You can list available meeting rooms from /availability API.
- Meeting rooms cannot be booked during below maintenance window.
- Maintenance can be configured at room level or location level
- Record 1,2,3 are at location level.
- Record 4,6 are at room level.

```editorconfig
( room_id, location_id, mnt_start_time, mnt_end_time) values ( 0, 1, '09:00', '09:15');
( room_id, location_id, mnt_start_time, mnt_end_time) values ( 0, 1, '13:00', '13:15');
( room_id, location_id, mnt_start_time, mnt_end_time) values ( 0, 1, '17:00', '17:15');
( room_id, location_id, mnt_start_time, mnt_end_time) values ( 1, 1, '11:00', '11:15');
( room_id, location_id, mnt_start_time, mnt_end_time) values ( 0, 2, '08:00', '08:15');
( room_id, location_id, mnt_start_time, mnt_end_time) values ( 7, 2, '12:00', '15:15');
```


Error codes used in this project.
```agsl
#Business validation error

B_0001-Start time cannot be equal or after end time
B_0002-Start or end time should be in interval of 15 minutes
B_0003-Minimum number of participants not met
B_0004-Reservation allowed only for current date
B_0005-Conference room not found for given location
B_0006-Conference room not available for this location or for this capacity
B_0007-Reservation overlaps maintenance window
B_0008-All conference room reserved for given time period. Check availability before reservation

#Generic error
G_0001-Input validation failure
G_0002-Generic exception
G_0003-Resource not found
G_0004-Internal server error
```

