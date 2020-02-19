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
5) I chose to use Spring WebFlux + Stream + MongoDB as base and leverage lots of Spring Cloud features to be "cloud agnostic". 
6) I also deployed the application in Cloud Foundry to validate the same.
7) Divided into 3 main components, which communicate via message broker



### Trade Offs

For sake of simplicity, I am building the 3 parts of the solution (Generator, Aggregation and UI) as all part of same ExerciseApp.
But the overall design, with the queues ( message broker ) allows that each component to be deployed as individual component, maintaining the integrity of the design.

For the reactive event driven implementation, I chose to use Stream and Flow, instead of message driven. This was more related to having issues with in memory Rabbit than technical choice.
And also, for this small use case, I focused more o the overall architectural design

I did not invest much on the UI. Honestly for lack of time to put on this exercise. And with this constraint I went to the thinner slice of the cake ( horizontal slicing ) and I left the UI feature for a further occasion


### Assumptions
Here are the assumptions I took to have a solution like this:
1. There would be this 3 categories to be considered. I have not design to a dynamic number of categories.
2. I assumed that the average would be from the last 3 orders. It can be "configurable" by the application properties
3. I assumed a simple way to calculate the rewards based on the 3 rewards scheme : I associated % of the points as rewards. That is also configurable on the application.yml


### Using the application
I invested time to have the local ( actually I consider this the "test instance" of my microservices or apps) which are built many times for all functional and performance testing usually.
You dont need anything pre installed in this case. For testing purpose I am using all in memory message broker and mongoDB database
For this exercise, as mentioned before, I simulated a "production environment" as deploying it on CF as well.

#### How to access it "in production"
I deployed it on my Cloud Foundry account. And can be access by :
1. Log of the generated orders ( https://leo-exercise-dev.cfapps.io/orders.html)
2. Moving Average stats ( https://leo-exercise-dev.cfapps.io/stats.html)

#### How to run it locally

To run locally you have to 
1) clone the repo : https://github.com/leonardo-s-mattos/event-streaming-exercise.git
2) got to the folder in which you cloned the repo
3) build it by running the command : " maven clean install ""
4) then run : "java - jar ./target/exercise-0.0.1-SNAPSHOT.jar --spring.profiles.active=local"

and then you can already see the application running:
1. Log of the generated orders ( https://leo-exercise-dev.cfapps.io/orders.html)
2. Moving Average stats ( https://leo-exercise-dev.cfapps.io/stats.html)






### Implementation order
This session here is more a guide for me ( the author ) guide my thoughts and dont forget high level tasks I would like to imply to make production ready.
To develop this solution, I will take the following steps:

1) Create the app structure with maven and basic spring boot app
2) Build a simple reactive web app with publisher to generate the events similar to described on problem statement
3) Create a CF environment and deployment pipeline with GitHub Actions
4) Extend REST API to store data and setup actuator .
5) Develop a simpler Stats service to send some data to reactive UI
6) Implement the Stats service
