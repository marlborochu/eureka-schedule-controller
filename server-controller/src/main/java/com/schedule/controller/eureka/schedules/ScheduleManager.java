package com.schedule.controller.eureka.schedules;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import com.schedule.controller.eureka.Constant;

/**
*
* @author marlboro.chu@gmail.com
*/
public abstract class ScheduleManager {
	
	
//	private static final Logger logger = LoggerFactory.getLogger(ScheduleManager.class);

	
	private Boolean hasPrimaryAgent = false;
	
	@Value("${"+Constant.KEY_SCHEDULE_CONTROLLER_SERVER_TYPE+"}")
	protected String serverType;
	
	public abstract void process();
	
	public Boolean isProcessor() {
		
		Boolean isPrimaryServer = false;
		if (!serverType.equals("P")) {
			if (serverType.equals("S")) {
								
				PeerAwareInstanceRegistry registry = EurekaServerContextHolder.getInstance().getServerContext()
						.getRegistry();
				Applications applications = registry.getApplications();
				Application app = applications.getRegisteredApplications(Constant.KEY_PRIMARY_CONTROLLER);
				if(app != null) {
					hasPrimaryAgent = true;
				}else {
					if(hasPrimaryAgent ) {
						isPrimaryServer = true;
					}
				}
			}
		} else {
			isPrimaryServer = true;
		}
		
		return isPrimaryServer;
	}
	
}
