package com.schedule.controller.eureka.schedules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.schedule.controller.eureka.service.JobService;

/**
*
* @author marlboro.chu@gmail.com
*/
public class JobControl extends ScheduleManager{

	@Autowired
	private JobService jobService;
	
	@Scheduled(fixedDelayString = "${15000:15000}", initialDelay = 45000)
	public void process() {
		if (isProcessor()) {
			jobService.load();
		}else {
			jobService.cancel();
		}
	}

}
