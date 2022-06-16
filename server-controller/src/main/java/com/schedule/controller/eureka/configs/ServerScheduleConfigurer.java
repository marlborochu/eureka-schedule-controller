package com.schedule.controller.eureka.configs;

import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.client.RestTemplate;

/**
*
* @author marlboro.chu@gmail.com
*/
@Configuration
@EnableScheduling
@EnableAsync
public class ServerScheduleConfigurer implements SchedulingConfigurer{

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(10);
        threadPoolTaskScheduler.setThreadNamePrefix("SController-");
        threadPoolTaskScheduler.initialize();
        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
	}
	
	@Bean(name="TaskScheduler")
    public ThreadPoolTaskScheduler agentTaskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(100);
        threadPoolTaskScheduler.setThreadNamePrefix("TaskScheduler-");
        threadPoolTaskScheduler.initialize();
        return threadPoolTaskScheduler;
    }
	
	@Bean("RemoteJobExecutor")
	public RestTemplate callRemoteJob(RestTemplateBuilder builder) {
		return builder
				.setConnectTimeout(Duration.ofMillis(5000))
				.setReadTimeout(Duration.ofMillis(30*60*1000))
				.build();
	}
	
}
