package com.lt.loadtest.loadtestagent.ui.listener;

import org.apache.log4j.Logger;

public class QueueMessageListener {
	private Logger logger = Logger.getLogger(QueueMessageListener.class);
	
    public void handle(String strMessage){
    	logger.info("Received: " + strMessage);
    }
}
