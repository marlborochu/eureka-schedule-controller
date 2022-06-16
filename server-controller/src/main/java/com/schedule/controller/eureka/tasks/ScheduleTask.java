package com.schedule.controller.eureka.tasks;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.appinfo.InstanceInfo;
import com.schedule.controller.eureka.models.JobInfo;
import com.schedule.controller.eureka.service.EurekaService;
import com.schedule.controller.eureka.utils.ControllerUtils;

/**
*
* @author marlboro.chu@gmail.com
*/
public class ScheduleTask implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(ScheduleTask.class);

	private Boolean isRun = false;
	private LocalDateTime beginExec;

	private RestTemplate restTemplate;
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	private JobInfo job;
	public void setJobInfo(JobInfo job) {
		this.job = job;
	}

	private EurekaService eurekaService;
	public void setEurekaService(EurekaService eurekaService) {
		this.eurekaService = eurekaService;
	}
	
	@Override
	public void run() {

		if (restTemplate == null) {
			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
			factory.setConnectTimeout(3000);
			factory.setReadTimeout(1000 * 60 * 30);
			restTemplate = new RestTemplate(factory);
		}
		
		if(!isRun && checkJobLimitTime(job)) {
			isRun = true;
			beginExec = LocalDateTime.now();
			try {
				
				job.setLastExecTime(
						beginExec.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
				job.setExecCount(job.getExecCount() == null ? 1 : job.getExecCount()+1);
				
				InstanceInfo instance = eurekaService.getInstance(job.getApplicationName());
				if(instance != null) {
					
					job.setExecuteServer(instance.getHomePageUrl());
					String url = instance.getHomePageUrl()+job.getApplicationPath();
					url = ControllerUtils.getInstance().filterURLPath(url);
					String res = execute(url);
					logger.debug("execute time "+beginExec+"~"+LocalDateTime.now());
					
				}else {
					
					job.setLastExecResponse("no available server found");
					job.setLastExecFlag("N");
					job.setFailCount(job.getFailCount() == null ? 1 : job.getFailCount()+1);
					logger.error("no available server found");
					
				}
			}finally {
				isRun = false;
			}	
		}

	}

	private String execute(String url) {

		logger.info("execute ["+url+"]");
		
		if (job.getReqMethod() == null)
			job.setReqMethod(HttpMethod.GET.toString());

		try {
			HttpHeaders requestHeaders = new HttpHeaders();
			if (job.getExtraHeader() != null) {
				logger.debug(job.getExtraHeader().toString());
				Iterator<String> ite = job.getExtraHeader().keySet().iterator();
				while (ite.hasNext()) {
					String headerKey = ite.next();
					requestHeaders.set(headerKey, job.getExtraHeader().get(headerKey));
				}
			}

			UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
			if (job.getReqMethod().equals(HttpMethod.GET.toString()) && job.getReqParams() != null) {

				Iterator<String> ite = job.getReqParams().keySet().iterator();
				while (ite.hasNext()) {
					String paramKey = ite.next();
					uriBuilder.queryParam(paramKey, job.getReqParams().get(paramKey));
				}

			}

			MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
			if (job.getReqParams() != null) {

				Iterator<String> ite = job.getReqParams().keySet().iterator();
				while (ite.hasNext()) {
					String paramKey = ite.next();
					params.add(paramKey, job.getReqParams().get(paramKey));
				}

			}

			HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(
					params, requestHeaders);
			ResponseEntity<String> resEntity = restTemplate.exchange(uriBuilder.toUriString(),
					HttpMethod.resolve(job.getReqMethod().toUpperCase()), requestEntity, String.class);
			if (resEntity.getStatusCode() == HttpStatus.OK) {
				logger.debug("response : [" + resEntity.getBody() + "]");
				job.setLastExecResponse(resEntity.getBody());
				job.setLastExecFlag("Y");
				job.setSuccessCount(job.getSuccessCount() == null ? 1 : job.getSuccessCount() + 1);
				return resEntity.getBody();
			} else {
				job.setLastExecResponse(resEntity.getBody());
				job.setLastExecFlag("N");
				job.setFailCount(job.getFailCount() == null ? 1 : job.getFailCount() + 1);
			}

		} catch (Exception e) {

			job.setLastExecResponse(e.getMessage());
			job.setLastExecFlag("N");
			job.setFailCount(job.getFailCount() == null ? 1 : job.getFailCount() + 1);
			logger.error("ProviderJobTaskException", e);
		}
		return null;
	}

	private Boolean checkJobLimitTime(JobInfo job) {
		
		if(StringUtils.isEmpty(job.getLimitTimeFormat())){
			return true;
		}
		DateTimeFormatter limitFormat = DateTimeFormatter.ofPattern(job.getLimitTimeFormat());
		String beginDate = job.getLimitBeginTime();
		String endDate = job.getLimitEndTime();
		String now = LocalTime.now().format(limitFormat);
		LocalTime dateTime = LocalTime.parse(now, limitFormat);
		boolean status = true;
		if(!StringUtils.isEmpty(job.getLimitTimeFormat()) && beginDate != null && !StringUtils.isEmpty(beginDate)) {
			status = dateTime.compareTo(LocalTime.parse(beginDate, limitFormat)) >= 0;
		}
		if(!StringUtils.isEmpty(job.getLimitTimeFormat()) && endDate != null && !StringUtils.isEmpty(beginDate) && status) {
			status = dateTime.compareTo(LocalTime.parse(endDate, limitFormat)) <= 0;
		}
		return status;
		
	}
}
