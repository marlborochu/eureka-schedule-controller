package com.schedule.controller.eureka.service.impl;

import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import com.schedule.controller.eureka.service.EurekaService;

/**
*
* @author marlboro.chu@gmail.com
*/
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
