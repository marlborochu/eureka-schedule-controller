package com.schedule.controller.eureka;

import java.util.Iterator;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
*
* @author marlboro.chu@gmail.com
*/
public class JobControlPostProcessor implements EnvironmentPostProcessor {

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

		try {

			Resource res = new ClassPathResource(Constant.KEY_JOB_CONTROLLER_CONFIG);
			Properties props = PropertiesLoaderUtils.loadProperties(res);
			// replace the value when it has the same key
			Iterator<Object> ite = props.keySet().iterator();
			while (ite.hasNext()) {
				String key = (String) ite.next();
				if (environment.containsProperty(key)) {
					props.put(key, environment.getProperty(key));
				}
			}

			String primaryAgent = props.getProperty(Constant.KEY_SCHEDULE_CONTROLLER_SERVER_PRIMARY_URL);
			String secondaryAgent = props.getProperty(Constant.KEY_SCHEDULE_CONTROLLER_SERVER_SECONDARY_URL);
			
			props.put("eureka.client.serviceUrl.defaultZone", primaryAgent+","+secondaryAgent);
			props.put("spring.application.name", environment.getProperty("spring.application.name","UNKNOW-SERVER"));

			props.put("eureka.instance.lease-expiration-duration-in-seconds", 30); 
			props.put("eureka.instance.lease-renewal-interval-in-seconds", 15);
			props.put("eureka.instance.prefer-ip-address", true);

			// final
			PropertiesPropertySource source = new PropertiesPropertySource("SCProperties", props);
			environment.getPropertySources().addFirst(source);

			System.out.println("initialize the configuration is success !!");
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("Failed to load the configuration!!!");
		}

	}

}