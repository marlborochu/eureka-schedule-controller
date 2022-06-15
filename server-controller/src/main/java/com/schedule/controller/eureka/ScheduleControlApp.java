package com.schedule.controller.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *
 * @author marlboro.chu@gmail.com
 */
@SpringBootApplication(scanBasePackages = { "com.schedule.controller.eureka" })
@EnableEurekaServer
public class ScheduleControlApp {
	
	public static void main(String[] args) {
		SpringApplication.run(ScheduleControlApp.class, args);
	}
	
}


