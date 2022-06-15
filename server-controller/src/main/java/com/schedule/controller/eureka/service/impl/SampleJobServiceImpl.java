package com.schedule.controller.eureka.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.schedule.controller.eureka.Constant;
import com.schedule.controller.eureka.models.JobInfo;
import com.schedule.controller.eureka.models.SampleJobInfo;
import com.schedule.controller.eureka.service.EurekaService;
import com.schedule.controller.eureka.service.JobService;
import com.schedule.controller.eureka.tasks.ScheduleTask;

/**
*
* @author marlboro.chu@gmail.com
*/
@ConditionalOnProperty(name = Constant.KEY_SCHEDULE_CONTROLLER_SAMPLE_MANAGER, 
	matchIfMissing = false, havingValue = "true")
@Service("SampleJobService")
public class SampleJobServiceImpl implements JobService {

	private static final Logger logger = LoggerFactory.getLogger(SampleJobServiceImpl.class);
		
	@Autowired
	private SampleJobInfo samplejobs;
	
	@Autowired
	private EurekaService eurekaService;
	
	@Autowired
	@Qualifier("RemoteJobExecutor")
	private RestTemplate restTemplate;
	
	@Autowired
	@Qualifier("TaskScheduler")
	private ThreadPoolTaskScheduler taskScheduler;
	
	private HashMap<String, HashMap<String, Object>> jobInfos = new HashMap<String, HashMap<String, Object>>();
	private HashMap<String, HashMap<String, Object>> taskInfos = new HashMap<String, HashMap<String, Object>>();
	
	private Boolean isRunning = false;
	
	synchronized Boolean running() {
		return isRunning;
	}
	
	@Override
	public void load() {
		
		logger.debug("********** "+samplejobs.getJobs());
		if(!running()) {
			isRunning = true;
			try {
				
				try {
					
					List<JobInfo> jobs = samplejobs.getJobs();
					synchronized (taskInfos) {
						ArrayList<String> newJobIds = new ArrayList<String>();
						for (JobInfo job : jobs) {
							job.setSignature(getObjectSinature(job));
							if (!taskInfos.containsKey(job.getJobId())) {
								createTask(job);
							} else {
								resetTask(job);
							}
							newJobIds.add(job.getJobId());
						}
					}
				}catch(Exception e) {
					logger.error("", e);
				}
				
			}finally {
				isRunning = false;
			}
		}
		
	}
	@Override
	public void reset() {
		cancel();
		load();
	}
	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public ArrayList<JobInfo> getScheduleInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	
	private String getObjectSinature(Object obj) {
		return DigestUtils.md5DigestAsHex(obj.toString().getBytes());
	}
	
	private void createTask(JobInfo job) {

		try {
			
			logger.info("begin create task ["+job.getJobId()+"]" );
			
			if(validateJobExecTime(job)) {
				
				ScheduleTask task = new ScheduleTask();
				task.setEurekaService(eurekaService);
				task.setJobInfo(job);
				task.setRestTemplate(restTemplate);
				
				CronTrigger cronTrigger = new CronTrigger(job.getCronExpression());
				ScheduledFuture<?> scheduleFuture = taskScheduler.schedule(task, cronTrigger);
				
				HashMap<String, Object> info = new HashMap<String, Object>();
				info.put(Constant.KEY_TASK_INFO, scheduleFuture);
				info.put(Constant.KEY_JOB_INFO, job);
				
				logger.info(scheduleFuture.toString());
				taskInfos.put(job.getJobId(), info);
				
			}else {
				logger.error("can't create task ["+job.getJobId()+"] begin time is ["+job.getBeginDateTime()+"] and end time is ["+job.getEndDateTime()+"]");
			}
		} catch (Exception e) {
			logger.error("ScheduleServiceException", e);
		}
	}

	private void resetTask(JobInfo job) {

		JobInfo oldJob = (JobInfo)taskInfos.get(job.getJobId()).get(Constant.KEY_JOB_INFO);
		if (!oldJob.getSignature().equals(job.getSignature()) || !validateJobExecTime(oldJob)) {

			((ScheduledFuture<?>)taskInfos.get(job.getJobId()).get(Constant.KEY_TASK_INFO)).cancel(true);
			taskInfos.remove(job.getJobId());
			createTask(job);

		}

	}
	
	private Boolean validateJobExecTime(JobInfo job) {
		
		String beginDate = job.getBeginDateTime();
		String endDate = job.getEndDateTime();
		LocalDateTime dateTime = LocalDateTime.now();
		boolean status = true;
		if(!StringUtils.isEmpty(job.getDateFormat()) && beginDate != null && !StringUtils.isEmpty(beginDate)) {
			status = dateTime.isAfter(LocalDateTime.parse(beginDate, DateTimeFormatter.ofPattern(job.getDateFormat())));
		}
		if(!StringUtils.isEmpty(job.getDateFormat()) && endDate != null && !StringUtils.isEmpty(beginDate) && status) {
			status = dateTime.isBefore(LocalDateTime.parse(endDate, DateTimeFormatter.ofPattern(job.getDateFormat())));
		}
		return status;
		
	}
	
}
