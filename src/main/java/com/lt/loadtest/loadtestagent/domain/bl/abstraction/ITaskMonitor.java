package com.lt.loadtest.loadtestagent.domain.bl.abstraction;

import com.lt.loadtest.loadtestagent.domain.model.InstanceTaskMinDeltaReport;
import com.lt.loadtest.loadtestagent.domain.model.InstanceTaskProcess;
import com.lt.loadtest.loadtestagent.domain.model.InstanceTaskProgress;

public interface ITaskMonitor {
	public InstanceTaskProcess getInstanceTaskProcess();
	public InstanceTaskProgress getInstanceTaskProgress();
	public InstanceTaskMinDeltaReport getInstanceTaskMinDeltaReport();
}
