package com.schedule.controller.eureka.models;

import java.util.Map;

import com.google.gson.Gson;

public class JobInfo {

	private String jobId;
	private String logId;
	private String applicationId;
	private String contextPath;
	private String beginDateTime;
	private String endDateTime;
	private String limitBeginTime;
	private String limitEndTime;
	private String cronExpression;
	private String dateFormat;
	private String limitTimeFormat;
	private String reqMethod;
	
	private String lastExecTime;
	private String lastExecResponse;
	private Integer execCount = 0;
	private Integer successCount = 0;
	private Integer failCount = 0;
	private String lastExecFlag;
	private String nextExecTime;
	private String timestamp;
	private String showFlag;
	
	private String signature;
	private String executeServer;
	
	private Map<String,String> reqParams;
	private Map<String,String> extraHeader; 


	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	

	public String getBeginDateTime() {
		return beginDateTime;
	}

	public void setBeginDateTime(String beginDateTime) {
		this.beginDateTime = beginDateTime;
	}

	public String getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}

	public String getLimitBeginTime() {
		return limitBeginTime;
	}

	public void setLimitBeginTime(String limitBeginTime) {
		this.limitBeginTime = limitBeginTime;
	}

	public String getLimitEndTime() {
		return limitEndTime;
	}

	public void setLimitEndTime(String limitEndTime) {
		this.limitEndTime = limitEndTime;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getLimitTimeFormat() {
		return limitTimeFormat;
	}

	public void setLimitTimeFormat(String limitTimeFormat) {
		this.limitTimeFormat = limitTimeFormat;
	}

	public Map<String,String> getReqParams() {
		return reqParams;
	}

	public void setReqParams(Map<String,String> reqParams) {
		this.reqParams = reqParams;
	}

	public Map<String,String> getExtraHeader() {
		return extraHeader;
	}

	public void setExtraHeader(Map<String,String> extraHeader) {
		this.extraHeader = extraHeader;
	}

	public String getReqMethod() {
		return reqMethod;
	}

	public void setReqMethod(String reqMethod) {
		this.reqMethod = reqMethod;
	}

	public String getLastExecTime() {
		return lastExecTime;
	}

	public void setLastExecTime(String lastExecTime) {
		this.lastExecTime = lastExecTime;
	}

	public String getLastExecResponse() {
		return lastExecResponse;
	}

	public void setLastExecResponse(String lastExecResponse) {
		this.lastExecResponse = lastExecResponse;
	}

	public Integer getExecCount() {
		return execCount;
	}

	public void setExecCount(Integer execCount) {
		this.execCount = execCount;
	}

	public String getLastExecFlag() {
		return lastExecFlag;
	}

	public void setLastExecFlag(String lastExecFlag) {
		this.lastExecFlag = lastExecFlag;
	}

	public String getNextExecTime() {
		return nextExecTime;
	}

	public void setNextExecTime(String nextExecTime) {
		this.nextExecTime = nextExecTime;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getShowFlag() {
		return showFlag;
	}

	public void setShowFlag(String showFlag) {
		this.showFlag = showFlag;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public String toString() {
		return new Gson().toJson(this);
	}

	public Integer getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}

	public Integer getFailCount() {
		return failCount;
	}

	public void setFailCount(Integer failCount) {
		this.failCount = failCount;
	}

	public String getExecuteServer() {
		return executeServer;
	}

	public void setExecuteServer(String executeServer) {
		this.executeServer = executeServer;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}
	
}
