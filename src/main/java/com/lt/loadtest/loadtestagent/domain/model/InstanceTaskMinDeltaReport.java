package com.lt.loadtest.loadtestagent.domain.model;

public class InstanceTaskMinDeltaReport {
	private long taskId;
    private long minPoint;
    private long transactionsNumDelta;
    private long errorNumDelta;
    private long responseTimeDelta;
	public long getTaskId() {
		return taskId;
	}
	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}
	public long getMinPoint() {
		return minPoint;
	}
	public void setMinPoint(long minPoint) {
		this.minPoint = minPoint;
	}
	public long getTransactionsNumDelta() {
		return transactionsNumDelta;
	}
	public void setTransactionsNumDelta(long transactionsNumDelta) {
		this.transactionsNumDelta = transactionsNumDelta;
	}
	public long getErrorNumDelta() {
		return errorNumDelta;
	}
	public void setErrorNumDelta(long errorNumDelta) {
		this.errorNumDelta = errorNumDelta;
	}
	public long getResponseTimeDelta() {
		return responseTimeDelta;
	}
	public void setResponseTimeDelta(long responseTimeDelta) {
		this.responseTimeDelta = responseTimeDelta;
	}
    
    
}
