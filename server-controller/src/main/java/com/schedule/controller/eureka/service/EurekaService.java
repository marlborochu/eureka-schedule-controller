package com.schedule.controller.eureka.service;

import org.springframework.stereotype.Service;

import com.netflix.appinfo.InstanceInfo;

/**
*
* @author marlboro.chu@gmail.com
*/
@Service
public interface EurekaService {
	
	public InstanceInfo getInstance(String appName);
}
