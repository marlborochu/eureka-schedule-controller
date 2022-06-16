## Schedule Controller

Use the existing functions of Eureka Server to implement a timing schedule controller that regularly calls remote services.

The HA (High Availability) architecture of the control system is designed in A/S (Active/Standby) mode and has automatic failover and automatic failback functions.

When there are multiple identical servers registered to the controller, the controller will call the API provided by the server in round-robin.

### Controller Server Configuration
Crete an empty String Boot web application using Eclipse. And add the following settings to manve's pom.xml file.

<sub>Add dependency. </sub>
```
<dependency>
	<groupId>com.schedule.controller</groupId>
	<artifactId>server-controller</artifactId>
	<version>1.0.1</version>
</dependency>
```
<sub>Add start-class.</sub>
```
<properties>
	<start-class>com.schedule.controller.eureka.ScheduleControlApp</start-class>
</properties>
```

Add the following settings to the application.properties file to differentiate between primary and secondary controllers in a high-availability architecture.
```
schedule.controller.server.primary.url=http://127.0.0.1:9001/schedule-controller/eureka/
schedule.controller.server.secondary.url=http://127.0.0.1:9003/schedule-controller/eureka/
```

When starting the spring boot application system, bring parameters to let the controller know whether it is the primary controler.

<sub>Start the Primary Controller</sub>
```
java -jar sample-controller-server-1.0.1.jar --server.port=9001 --schedule.controller.server.type=P 
```
<sub>Start the Secondary Controller</sub>
```
java -jar sample-controller-server-1.0.1.jar --server.port=9003 --schedule.controller.server.type=S
```

<img width="735" alt="Capture" src="https://user-images.githubusercontent.com/24667449/173980217-d7fc1538-ec4e-4984-b2fd-b1affebd221d.PNG">

When you log in to the Eureka console of the primary or secondary controller, you can view the registered controllers.

<img width="932" alt="Capture" src="https://user-images.githubusercontent.com/24667449/173981478-9bdcf77e-14d8-4451-9ad2-9711ff9193da.PNG">


### API Server Configuration
Add the following settings to the existing API Server so that it can be registered with the Eureka Server.

<sub>Add dependency. </sub>
```
<dependency>
	<groupId>com.schedule.controller</groupId>
	<artifactId>job-controller</artifactId>
	<version>1.0.1</version>
</dependency>
```
<sub>Add controller information to application.properties file.</sub>
```
schedule.controller.server.primary.url=http://127.0.0.1:9001/schedule-controller/eureka/
schedule.controller.server.secondary.url=http://127.0.0.1:9003/schedule-controller/eureka/
```
After restarting the API Server, the Eureka console can see the message that the API Server has been registered.

<img width="798" alt="Capture" src="https://user-images.githubusercontent.com/24667449/173985846-40a6dcd4-dafe-4dc8-8039-5185dc0e5b6c.PNG">


### Schedule Job Configuration
Create a new file sample-jobs.yml under the controller server as shown in the figure below.

<img width="492" alt="Capture" src="https://user-images.githubusercontent.com/24667449/173983875-900739d4-eac1-4d5f-9d47-97f8227b808f.PNG">

Then add the API and scheduling information to the file.
```
jobs:
 - jobId: "0001"
   applicationName: SAMPLE-API
   applicationPath: /job-controller/SAMPLE0001
   cronExpression: "*/15 * * * * *"
```

When more than two identical API Servers are started and both are registered with Eureka, from the API Server log messages, we can know that the controller will only call one of the API Servers by round-robin.

<sub>Two identical API Servers registered with Eureka</sub>

<img width="847" alt="Capture" src="https://user-images.githubusercontent.com/24667449/173987342-f98a724d-e9e1-468e-8f60-d0cc9d555ae0.PNG">

<sub>Call API Server by Round-Robin</sub>

<img width="592" alt="Capture" src="https://user-images.githubusercontent.com/24667449/173987794-cf8028df-499d-42b0-968f-b3a623a117b2.PNG">

### For more information, please refer to the sample project

[sample-eureka-schedule-controller](https://github.com/marlborochu/sample-eureka-schedule-controller)

