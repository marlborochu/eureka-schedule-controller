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
public class ScheduleControlPostProcessor implements EnvironmentPostProcessor {

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

		try {

			Resource res = new ClassPathResource(Constant.KEY_SCHEDULE_CONTROLLER_CONFIG);
			Properties props = PropertiesLoaderUtils.loadProperties(res);
			// replace the value when it has the same key
			Iterator<Object> ite = props.keySet().iterator();
			while (ite.hasNext()) {
				String key = (String) ite.next();
				if (environment.containsProperty(key)) {
					props.put(key, environment.getProperty(key));
				}
			}

			if ("P".equalsIgnoreCase(props.getProperty(Constant.KEY_SCHEDULE_CONTROLLER_SERVER_TYPE))) {
				props.put("eureka.client.serviceUrl.defaultZone",
						props.getProperty(Constant.KEY_SCHEDULE_CONTROLLER_SERVER_SECONDARY_URL));
				props.put("spring.application.name", Constant.KEY_PRIMARY_CONTROLLER);
			} else if ("S".equalsIgnoreCase(props.getProperty(Constant.KEY_SCHEDULE_CONTROLLER_SERVER_TYPE))) {
				props.put("eureka.client.serviceUrl.defaultZone",
						props.getProperty(Constant.KEY_SCHEDULE_CONTROLLER_SERVER_PRIMARY_URL));
				props.put("spring.application.name", Constant.KEY_SECONDARY_CONTROLLER);
			}

			if ("Y".equalsIgnoreCase(props.getProperty(Constant.KEY_EUREKA_AUTO_CONFIG_PARAMETER, "Y"))
					|| "true".equalsIgnoreCase(props.getProperty(Constant.KEY_EUREKA_AUTO_CONFIG_PARAMETER))) {
				// props.put("eureka.server.enable-self-preservation", false);
				props.put("eureka.instance.prefer-ip-address", false);
				props.put("eureka.instance.lease-expiration-duration-in-seconds", 15);
				props.put("eureka.instance.lease-renewal-interval-in-seconds", 5);
				props.put("eureka.server.eviction-interval-timer-in-ms", 1000 * 15);
				props.put("eureka.server.registry-sync-retries", 3);
				props.put("eureka.server.registry-sync-retry-wait-ms", 1000);
				props.put("eureka.server.wait-time-in-ms-when-sync-empty", 1000 * 30);
			}

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