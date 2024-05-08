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

Spring Doc is integrated in application hence API documentation are available at swagger.
Application can be directly tested in swagger.

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
Out of scope
- API security scoped out for this project. 
- Auditing fields like createdBy and createdDate are not implemented as we dont have security.


Quick details about API:

- Reservation API: 
  - Added meeting data so we can extend the API to book meeting rooms for future date with minimul change.
  - Added locationID and default to 1.  We can create room for multiple location and reserve. 
  - Location details is storied in separate table
  - Maintenance is extended at room level as well. We can close a room for maintenance partially or for entire day as well.


- Availability API:
  - Location application for this API as well and its option value default to locationId: 1


Information about preloaded data:

- We have two location 1, 2.
- Few meeting rooms created for each location. You can list available meeting rooms from /availability API.
```editorconfig
ROOM_NAME	ROOM_CAPACITY	LOCATION_ID
Amaze		3		1
Beauty		7		1
Inspire		12		1
Strive		20		1
Edge		15		2
Square		20		2
Circle		20		2
```
- Meeting rooms cannot be booked during below maintenance window.
- Maintenance can be configured at room level or location level
- Record 1,2,3 are at location level.
- Record 4,6 are at room level.

```editorconfig
ROOM_ID LOCATION_ID MNT_START_TIME  MNT_END_TIME
0	1	    9:00:00         9:15:00
0	1	    13:00:00        13:15:00
0	1	    17:00:00	    17:15:00
1	1	    11:00:00	    11:15:00
0	2	    8:00:00         8:15:00
6	2	    12:00:00        15:15:00
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

