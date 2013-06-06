package com.lt.loadtest.loadtestagent.application;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class TaskProcessMonitoringJob extends QuartzJobBean {

    private Logger logger = Logger.getLogger(TaskProcessMonitoringJob.class);

    AmqpTemplate amqpTemplate;
    String queueName;
    
    public void setAmqpTemplate(AmqpTemplate amqpTemplate){
    	this.amqpTemplate = amqpTemplate;
    }
    
    public void setQueueName(String queueName){
    	this.queueName = queueName;
    }
    
    @Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		logger.info("TaskProcessMonitoringJob: Now is " + new Date());
		this.amqpTemplate.convertAndSend(this.queueName, "foo1111"+new Date());
		logger.info(String.format("Sent Msg to %s at %s", this.queueName, new Date()));
	}

}
