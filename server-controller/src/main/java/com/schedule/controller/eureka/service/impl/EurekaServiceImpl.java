package com.schedule.controller.eureka.service.impl;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import com.schedule.controller.eureka.service.EurekaService;

@Service
public class EurekaServiceImpl implements EurekaService{
	
	private static final Logger logger = LoggerFactory.getLogger(EurekaServiceImpl.class);
	
	private HashMap<String,Integer> appIndex = new HashMap<String,Integer>();
	
	@Autowired
	private Environment env;
	
	
	public InstanceInfo getInstance(String appName) {
		
		try {
			PeerAwareInstanceRegistry registry = EurekaServerContextHolder.getInstance().getServerContext().getRegistry();
		    Applications applications = registry.getApplications();
		    Application app = applications.getRegisteredApplications(appName);
		    if(app != null) {
		    	List<InstanceInfo> instances = app.getInstances();
		    	synchronized(appIndex) {
		    		if(!appIndex.containsKey(appName)) {
		    			appIndex.put(appName, 0);
		    		}
		    		int index = appIndex.get(appName);
		    		if(index >= instances.size()) index = 0;
		    		InstanceInfo instance = instances.get(index++);		    
		    		appIndex.put(appName, index);
		    		return instance;
		    	}
		    }else {
		    	logger.error("no available registered applications ["+appName+"] ");
		    }
		}catch(Exception e) {
			logger.error("AgentServiceException",e);
		}    
		return null;
	}
	
}
