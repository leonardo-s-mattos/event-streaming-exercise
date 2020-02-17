# event-streaming-exercise
This repository is dedicated to exercise to implement an application that consumes transactional data in form of Json from a stream and agregate result ( different averages ) and present real time results.


### Problem Statement
The problem used as use case for this exercise can be found [here](/Problem-Statement.md)

### Design considerations:
To come up with the design of this solution, I used some ground principles. It also took in consideration the problem statement NFR's.

1) To deal with the given capacity of millions of records per day, I chose an reactive base architecture, using some well known patterns and libraries included in Spring Framework.
2) As it was asked for a closest to production as possible, and aiming to deliver high quality user experience, we must case about the system's responsiveness and resiliency ( ability to respond under failure ).
3) As one of the reactive patterns, I chose the streaming architectural technique, due to the characteristic of low latency and high throughput, achieved using the isolation principle together with the data pipeline.
4) To make it truly reactive design, I used the SSE for the UI
5) I chose to use Spring WebFlux + RabbitMQ + MongoDB as base and leverage lots of Spring Cloud features to be "cloud agnostic". 
6) I also deployed the application in Cloud Foundry to validate the same.
7) Divided into 3 main components, which communicate via message broker



### Trade Offs

For sake of simplicity, I am building the 3 parts of the solution (Generator, Aggregation and UI) as all part of same ExerciseApp.
But the overall design, with the queues ( message broker ) allows that each component to be deployed as individual component, maintaining the integrity of the design.

Kafka gives a better throughput than RabbitMQ. But to have in memory broker, I coded with RabbitMQ.


### Assumptions


### Using the application
I invested time to have the local ( actually I consider this the "test instance" of my microservices or apps) which are built many times for all functional and performance testing usually.
You dont need anything pre installed in this case. For testing purpose I am using all in memory message broker and mongoDB database
For this exercise, as mentioned before, I simulated a "production environment" as deploying it on CF as well.

#### How to access it "in production"

#### How to run it locally







### Implementation order
This session here is more a guide for me ( the author ) guide my thoughts and dont forget high level tasks I would like to imply to make production ready.
To develop this solution, I will take the following steps:

1) Create the app structure with maven and basic spring boot app
2) Build a simple reactive web app with publisher to generate the events similar to described on problem statement
3) Create a CF environment and deployment pipeline with GitHub Actions
4) Extend REST API to store data and setup actuator and swagger ui.
