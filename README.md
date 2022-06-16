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


### Failover

<img width="466" alt="failover and failback with application ii" src="https://user-images.githubusercontent.com/24667449/172831173-7f3edfc8-ea36-4dee-8c0a-8d8002b1d41d.png">

