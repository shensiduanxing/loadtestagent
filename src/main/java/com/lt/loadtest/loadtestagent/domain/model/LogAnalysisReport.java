package com.lt.loadtest.loadtestagent.domain.model;

import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Domain (Domain Driven Design)
 * @author liutao
 *
 */
public class LogAnalysisReport {
	private static final Log logger = LogFactory.getLog(LogAnalysisReport.class);
	
	private SortedMap<String, Integer> tpmReport=new TreeMap<String, Integer>();
	private SortedMap<String, Integer> responseTimeReport=new TreeMap<String, Integer>();
	
	public void merge(LogAnalysisReport logAnalysisReport){
		if(logAnalysisReport!=null){
			tpmReport.putAll(logAnalysisReport.getTPMReport());
			responseTimeReport.putAll(logAnalysisReport.getResponseTimeReport());
		}else{
			//logger.error(String.format("Invalid TPS Report data: secPoint=%s, requestsNum=%s", secPoint, requestsNum));
		}
	}
	
	public void setTPMReport(SortedMap<String, Integer> tpmReport){
		this.tpmReport = tpmReport;
	}
	
	public SortedMap<String, Integer> getTPMReport(){
		return this.tpmReport;
	}
	
	public void setResponseTimeReport(SortedMap<String, Integer> responseTimeReport){
		this.responseTimeReport = responseTimeReport;
	}
	
	public SortedMap<String, Integer> getResponseTimeReport(){
		return this.responseTimeReport;
	}
}
