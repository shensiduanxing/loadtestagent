package com.lt.loadtest.loadtestagent.domain.bl.abstraction;

import com.lt.loadtest.loadtestagent.domain.model.TestTask;

public interface ITaskExecutor {
	public void startIntanceTask(TestTask testTask);
	public void stopInstanceTask(TestTask testTask);
}
