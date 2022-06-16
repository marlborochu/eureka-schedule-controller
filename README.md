## Schedule Controller

Use the existing functions of Eureka Server to implement a timing schedule controller that regularly calls remote services.

The HA (High Availability) architecture of the control system is designed in A/S (Active/Standby) mode and has automatic failover and automatic failback functions.

When there are multiple identical servers registered to the controller, the controller will call the API provided by the server in round-robin.

### Sample
<sub>Starting Primary Server</sub>
```
java -jar target\sample-primary-server-1.0.1.jar 
```
<sub>Starting Secondary Server</sub>
```
java -jar target\sample-primary-server-1.0.1.jar --server.port=9002 --schedule.controller.server.type=S
```

### Failover

<img width="466" alt="failover and failback with application ii" src="https://user-images.githubusercontent.com/24667449/172831173-7f3edfc8-ea36-4dee-8c0a-8d8002b1d41d.png">

