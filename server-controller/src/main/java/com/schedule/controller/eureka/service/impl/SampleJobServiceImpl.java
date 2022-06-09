package com.schedule.controller.eureka.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.schedule.controller.eureka.models.JobInfo;
import com.schedule.controller.eureka.models.SampleJobInfo;
import com.schedule.controller.eureka.service.JobService;

@Service
public class SampleJobServiceImpl implements JobService {

	private static final Logger logger = LoggerFactory.getLogger(SampleJobServiceImpl.class);
//	private HashMap<String, HashMap<String, Object>> jobInfos = new HashMap<String, HashMap<String, Object>>();
	@Autowired
	private SampleJobInfo samplejobs;
	
	@Override
	public void load() {
		logger.info(samplejobs.getJobs()+"");
	}
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
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

	
}
