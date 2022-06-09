package com.schedule.controller.eureka.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.schedule.controller.eureka.models.JobInfo;


@Service
public interface JobService {
	
	public void load();
	public void reset();
	public void cancel();
	public ArrayList<JobInfo> getScheduleInfo();
	
}
