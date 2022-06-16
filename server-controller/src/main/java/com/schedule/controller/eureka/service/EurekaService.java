package com.schedule.controller.eureka.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.netflix.appinfo.InstanceInfo;

@Service
public interface EurekaService {
	
	public InstanceInfo getInstance(String appName);
}
