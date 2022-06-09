package com.schedule.controller.eureka.models;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:job-list.yml")
@ConfigurationProperties(prefix = "jobs")
public class SampleJobInfo {
	
	private List<JobInfo> jobs;

	public List<JobInfo> getJobs() {
		return jobs;
	}

	public void setJobs(List<JobInfo> jobs) {
		this.jobs = jobs;
	}
	
	
}
