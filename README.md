# Projector Management System

## Background:
Assume an office that has 3 projectors and 5 teams utilizing them. If an administrator were to manage access to the projectors for those 5 teams, it would be inefficient - here’s the process roughly:
* Whenever a team requires a projector, one of the team members sends an email to the admin mentioning the start and end times within which they want to use the projector. 
*	The admin goes through the requests in a first come, first served fashion and sees if the requested slot can be accommodated or not for a particular projector; if not, they look at the next projector. 
*	In the end either they allocate a projector and reply to the team with the projector’s name or they simply deny the request, saying that it cannot be accommodated. Also, sometimes they recommend the closest available time to the original requested time, so that the team can consider changing the time of their meeting and request again. 

## Requirement:
Design and build a simple “Projector Management System” that manages access to these 3 projectors including the following: 
* Design the database, service, and controllers and provide a REST endpoint that lets people check the availability of projectors, request a projector, cancel a request,... 
* Separate the responsibilities of different classes and methods. 
* Use efficient data structures to manage and see the availability of the projectors and extensibility of future requirements (for example, instead of simply denying a request when there is no availability, we should be able suggest the closest times to initial request when a projector could be allocated).

## Design:

* Database design:
Three new tables:
```
projector: id(pk), name
team: id(pk), name
booking: projector_id(fk of projector table), team_id (fk of team table), start_time, end_time
```
The above new tables are created automatically during Spring Boot application start.

* REST APIs design:
```
POST     -  /v1/projectors                                                                      Create a projector
GET      -  /v1/projectors                                                                      Get a list of projectors
GET      -  /v1/projectors/{id}                                                                 Get a projector with an ID
DELETE   -  /v1/projectors/{id}                                                                 Delete a projector with an ID
POST     -  /v1/teams                                                                           Create a team
GET      -  /v1/teams                                                                           Get a list of teams
GET      -  /v1/teams/{id}                                                                      Get a team with an ID
DELETE   -  /v1/teams/{id}                                                                      Delete a team with an ID
POST     -  /v1/bookings                                                                        Create a team
GET      -  /v1/bookings                                                                        Get a list of projector bookings
GET      -  /v1/bookings/{id}                                                                   Get a projector booking with an ID
DELETE   -  /v1/bookings/{id}                                                                   Cancel a projector booking with an ID
GET      -  /v1/bookings/projectors?start_time={start_time}&end_time={end_time}&[advice=true]   Get available projector with requested time slot
```

## Technologies:
```
1.Spring Boot - 2.1.2.RELEASE
2.Spring Data JPA - 2.1.2 RELEASE (includes hibernate)
3.MYSQL - 8.0.13
4.springfox-swagger - 2.9.2 (used for rest api documenation)
5.JDK - 1.10
6.Maven - 4.0
```

## Deployment
1. Clone the application
```
git clone 
```
2. Create Mysql database
```
create database projector_booking
```
3. Change *spring.datasource.username* and *spring.datasource.password* in application.properties
4. Build and run the application using maven
```
mvn spring-boot:run
```
5. Application will start running at http://localhost:8080
6. Application REST API document can be found at http://localhost:8080/swagger-ui.html

## Testing
Application REST API testing including the following scenarios have been done on both unit tests and Postman. All the screenshots have been captured in ProjectorBooking_Screenshots.doc
```
1.	Team1 requests a projector on a given date from 10:00 am to 12:00 pm => (System can assign Projector1)
2.	Team2 requests a projector on the same day from 10:30 am to 11:30 am => (System cannot assign Projector1 as it is overlapping with Team1’s reserved slot, but it can assign Projector2)
3.	Team3 requests a projector from 11:10 am to 12:00 pm => (System cannot assign Projector1 or Projector2 but can assign Projector3, etc.)
4.	Team4 requests a projector between 11:00 am - 11:30 am => (System cannot assign any projectors now since they’re all taken)
5.	Team1 cancels their 10am - 12pm slot => System frees up Projector1
6.	Team4 wants a projector between 10am and 11am => System assigns Projector1
```


