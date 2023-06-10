## How to run this

1. PreRequisites : Ensure maven, java 17, npm to be installed in your system along with docker
2. Clone the repo to your local machine.
3. Build the Applications: Open a terminal and navigate to the cloned repository. Run the following command to build all the apps using Maven `mvn clean install`.
4. Start the Docker Containers: After the build process is complete, run the following command to start the Docker containers `docker compose up`.
5. Navigate to http://localhost:8081 and enjoy the show!!!

Both the above steps may take couple of minutes to complete so please be patient.

## Assumptions:

1. I am assuming the docker is runned in your local machine, so hard coded the URLs in the frontend to hit localhost.
2. Currently the producer, ActiveMQ and the fanout service lives in the notification-fanout-service application, I used an inmemory implementation of ActiveMQ.
3. Producer randomly picks few user and sends them a random text in activity once in every 5 seconds.
4. While implementing, I implemented all components as a single node (container) components. Probably isolating queue, producer from the fanout service also makes it horizontally scalable.
5. In interview I talked about template evaluation at the fanout service, I haven't implemented it as I thought that doesn't really makes a big difference in the overall architecture.
6. Assumed a system with 100 users, with names user-0, user-1,.....user-99.
7. No Authentication and Authorization.

## For design in interview and design implemented as well as website walk through

Please go through this [Microsoft's Whiteboard](https://wbd.ms/share/v2/aHR0cHM6Ly93aGl0ZWJvYXJkLm1pY3Jvc29mdC5jb20vYXBpL3YxLjAvd2hpdGVib2FyZHMvcmVkZWVtLzdlM2U5MjQzNGE1YjQ2NjViOTdiYzUwMmRjOTkxMTUxX0JCQTcxNzYyLTEyRTAtNDJFMS1CMzI0LTVCMTMxRjQyNEUzRF8zNmU4MjgyNS03NTE4LTQxNzctOWQ5ZC1iOWUyZTY5NTdmNTk=) alternatively I exported it as pdf and stored as PDF in this repository with name as Typeface project 2 design.pdf (preffer whiteboard over pdf as some details may missout during conversion to pdf).

## Details of each component.

### 1. activity-fanout-service (spring boot app)

1. Reads the message from the queue, (message has text and intended recepients).
2. Persists the message to DB (Mongo).
3. Loops over the receipents and updates the activity-feed-cache's, feed list for each receipent with the pk of the message record obtained in step 2.
4. Publishes the activity to the notification-cache expecting if the recepits are listening to the cache (via notification-session-service).

### 2. user-feed-cache (redis)

1. Holds mapping of <user : List<ActivityId>> in reverse chronological order.
2. Only this list data is served to the end user, upon the HttpGet call, the list is trimmed to a size 500 when ever it reaches a size of 550. So, previous activities beyond this 500 are not shown to user currently.

### 3. activity-db (mongodb)

1. Holds the activity records. Currently I am not persisting the receipents of each activity, as the reverse index of (user -> List<Activity>) needs to be persisted.

### 4. activity-cache (redis)

1. This is cache over activity-db, used while retriving the activity records (upon the HttpGet calls).

### 5. activity-api-service (spring boot app)

1. This service serves the HTML conetent to the user.
2. Serves the initial HttpGet request, by retriving the corresponding user's activity feed from activity-feed-cache and retriving activity message from (activity-cache -> activity db) setup.

### 6. notification-cache

1. Possibly tries to holds a pubsub for each user.
2. Upon getting a publish call from fanout-service, this is current redis pub sub behaviour.
   i. If the user is active and connected to this cache (via notification service), the cache tries to deliver the message to the user.
   ii. If the user is inactive drops the message.

### 7. notification-session-service

1. When user launch the webpage and impersonate as the exisiting users, the frontend opends a webscoket connection to this service.
2. This service act like a pipe between notification-cache and the active user.
3. User can impersonate via many browser, and all browser tabs are notified, and treated as different devices.

### 8. frontend / user

1. User opens the link [http://localhost:8081](http://localhost:8081) in a browser.
2. Upon user impersonating as some registed user(say user-58), the fronend fetches the activity list for him/her, as well opens a websocket connect to notification-session-service to listen for live activities getting posted.

## Notes:

Currently the producer randomly sends messages to all the users, upon testing observed that an average user is likely to receive altleast a message in a minute. So, once you impersonate as a user, please wait for couple of minutes for producer to randomly send the user you impersonate a message for which your will get a notification.

Please feel free to reach out to me for any details.

### My-learnings

1. Understood websockets, implemented communication between the client/server using them.
2. Understood Redis, integrating it with spring boot, this is very first time I used it, I used lists, pubsub and used it as cache, I really saw the difference in round about time getting reduce from 1050 ms to 53ms upon caching the data.
3. Experimented and learnt material ui components, and how to integrate them.

### Challenges

1. Came to know the issues after significant investigation that, with the latest jakarta sepc changes requires one to put additional jars to activate ActiveMQ, later found a guy raised a cr for this(https://github.com/spring-guides/gs-messaging-jms/pull/35).
2. There are bunch of guides for integrating spring boot with websockets via STOMP and message broker, it required me to step back and think I need to understand websockets more in this context than using with stomp, that way I unserstood websockets some more and I implemented notification session service with websockets myself instead of deligating this to spring boot/STOMP broker

## Good to have but I missed due to time constraints.

1. User logged in with proper auth.
2. Integrate the component services with service discovery.
3. User some config store to manage the config paramters currently I managed them via .env files.
