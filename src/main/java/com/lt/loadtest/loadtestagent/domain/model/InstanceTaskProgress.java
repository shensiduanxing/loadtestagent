package com.lt.loadtest.loadtestagent.domain.model;

public class InstanceTaskProgress {
	private String taskId;
	private String hostInstanceId;
    private int sentNum;
    private int totalNum;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getHostInstanceId() {
		return hostInstanceId;
	}
	public void setHostInstanceId(String hostInstanceId) {
		this.hostInstanceId = hostInstanceId;
	}
	public int getSentNum() {
		return sentNum;
	}
	public void setSentNum(int sentNum) {
		this.sentNum = sentNum;
	}
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
    
    
}
