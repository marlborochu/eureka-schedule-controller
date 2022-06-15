package com.schedule.controller.eureka.schedules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;

import com.schedule.controller.eureka.service.JobService;

/**
*
* @author marlboro.chu@gmail.com
*/
public class SampleScheduleManager extends ScheduleManager{

	private static final Logger logger = LoggerFactory.getLogger(SampleScheduleManager.class);
	
	@Autowired
	@Qualifier("SampleJobService")
	private JobService jobService;
	
	@Scheduled(fixedDelayString = "${15000:15000}", initialDelay = 15000)
	public void process() {
		logger.debug("JobControl start...");
		if (isProcessor()) {
			jobService.load();
		}else {
			jobService.cancel();
		}
	}

}
