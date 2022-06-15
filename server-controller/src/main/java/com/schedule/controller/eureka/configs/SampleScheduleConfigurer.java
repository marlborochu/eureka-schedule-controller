package com.schedule.controller.eureka.configs;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import com.schedule.controller.eureka.Constant;
import com.schedule.controller.eureka.factories.YamlPropertySourceFactory;
import com.schedule.controller.eureka.models.SampleJobInfo;
import com.schedule.controller.eureka.schedules.SampleScheduleManager;
import com.schedule.controller.eureka.schedules.ScheduleManager;

@ConditionalOnProperty(name = Constant.KEY_SCHEDULE_CONTROLLER_SAMPLE_MANAGER, 
	matchIfMissing = false, havingValue = "true")
@Configuration
@PropertySource(value = "classpath:sample-jobs.yml", factory = YamlPropertySourceFactory.class)
public class SampleScheduleConfigurer {
	
	@Bean
	public ScheduleManager startSampleScheduleManager() {
		return new SampleScheduleManager();
	}
	
	@Bean
	@ConfigurationProperties
	public SampleJobInfo createSampleJobInfo() throws Exception{
		return new SampleJobInfo();		
	}
	
}
