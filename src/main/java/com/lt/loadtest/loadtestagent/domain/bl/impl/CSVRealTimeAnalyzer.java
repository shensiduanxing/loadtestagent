package com.lt.loadtest.loadtestagent.domain.bl.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lt.loadtest.loadtestagent.domain.model.LogAnalysisReport;
import com.lt.loadtest.loadtestagent.infrastructure.common.file.FileUtil;

public class CSVRealTimeAnalyzer {
	private static final Log logger = LogFactory.getLog(CSVRealTimeAnalyzer.class);
    private static final int POLLING_INTERVAL = 10000;
	
	public static void analzyeCSV(String logFilePath){
		//./jmeter -n -t ~/Blur/cloudpi/118_http_200_30000_20130516102132.jmx
		//String logFilePath = "/tmp/118_http_C200_L30000_20130516102132.csv";
		//logFilePath = "/Users/marsliutao/Blur/cloudpi/sample1.csv";
		File file= new File(logFilePath);
		long startPos = 0;
		long endPos = 0;

		ExecutorService executorService = Executors.newCachedThreadPool();
		List<Future<LogAnalysisReport>> resultList = new ArrayList<Future<LogAnalysisReport>>();
		
		FileInputStream fis = null;
		FileChannel fc = null;
		long fileSize = 0;
		
		try {
			fis = new FileInputStream (file);
			fc = fis.getChannel();
			fileSize = fc.size();
			endPos = fileSize;
			System.out.println("fileSize="+fileSize);
			while(true && startPos<endPos){
				try {
					//Step1: Get current analayzable lines
					//1.1 Get lastEofPos
					//1.2 Get lines from start pos to lastEofPos
					long lastEOFPos = FileUtil.getLastEOFPos(file, startPos, endPos - startPos);
					long size = lastEOFPos - startPos;
					
					System.out.println(String.format("start=%s,endPos=%s,lastEOFPos=%s,size=%s", startPos, endPos, lastEOFPos, size));
					
					MappedByteBuffer filedata = fc.map (MapMode.READ_ONLY, startPos, size);
					fis.close();
					
					Callable<LogAnalysisReport> worker = new LogAnalyzeThread(filedata, size);
					Future<LogAnalysisReport> submit = executorService.submit(worker);
					resultList.add(submit);
					
					startPos = lastEOFPos+1;
					
					Thread.sleep(POLLING_INTERVAL);
					
					fis = new FileInputStream (file);
					fc = fis.getChannel();
					fileSize = fc.size();
					endPos = fileSize;
					System.out.println(String.format("start=%s,endPos=%s", startPos, endPos));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					logger.error("Can not find jmeter log file", e);
				} catch (IOException e) {
					e.printStackTrace();
					logger.error("IO Exception when read jmeter log file", e);
				} 
				
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("======================");
		for (Future<LogAnalysisReport> future : resultList) {
		      try {
		    	  LogAnalysisReport logAnalysisReport = future.get();
		    	  SortedMap<String, Integer> tmpReport = logAnalysisReport.getTPMReport();
		    	  for(Entry<String,Integer> entry : tmpReport.entrySet()){
		    		  String key = entry.getKey();
		    		  Integer value = entry.getValue();
		    		  System.out.println("key="+key+", value="+value);
		    	  }
		      } catch (InterruptedException e) {
		        e.printStackTrace();
		      } catch (ExecutionException e) {
		        e.printStackTrace();
		      }
		}
		executorService.shutdown();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String logFilePath = "/tmp/118_http_C200_L30000_20130516102132.csv";
		analzyeCSV(logFilePath);
	}

}

class LogAnalyzeThread implements Callable<LogAnalysisReport> {
    
	private MappedByteBuffer filedata;
	private long size;
	public LogAnalyzeThread(MappedByteBuffer filedata, long size){
		this.filedata = filedata;
		this.size = size;
	}
	
	public LogAnalysisReport call() {
		int count=0;
		LogAnalysisReport analysisReport = new LogAnalysisReport();
		try {
			SortedMap<String, Integer> tpmReport= new TreeMap<String, Integer>();
			HashMap<String, Integer> minTotalResponseTimeMap = new HashMap<String, Integer>();
			SortedMap<String, Integer> responseTimeReport= new TreeMap<String, Integer>();
			
			byte[] buffer = new byte[(int)this.size];
			this.filedata.get(buffer);
		   
			StringBuilder sb = new StringBuilder();
			
		    BufferedReader in = new BufferedReader(
		    		new InputStreamReader(
		    				new ByteArrayInputStream(buffer)));
	        
		    for (String line = in.readLine(); line != null; line = in.readLine()) {
		      sb.append(line).append("\n");
		      String[] fields = line.split(",");
				if(fields!=null && fields.length==10){
					//System.out.println(line);
					String strMSTimePoint = fields[0];
	          	    String strReqResponseTime = fields[1];
	          	    if(strMSTimePoint!=null && strMSTimePoint.length()==13){
		            	    long timePoint = Long.parseLong(strMSTimePoint);
		            	    long minTimePoint = getMinTime(Long.parseLong(strMSTimePoint));
		            	    //System.out.println("minTimePoint="+minTimePoint);
		            	    Integer requestNumbersOfTheMin = tpmReport.get(String.valueOf(minTimePoint));
		            	    if(requestNumbersOfTheMin == null){
		            	    	tpmReport.put(String.valueOf(minTimePoint), Integer.valueOf(1));
		            	    }else {
		            	    	requestNumbersOfTheMin++;
		            	    	tpmReport.put(String.valueOf(minTimePoint), Integer.valueOf(requestNumbersOfTheMin));
		            	    }
		            	    
		            	    Integer requestResponseTime = minTotalResponseTimeMap.get(String.valueOf(minTimePoint));
		            	    if(requestResponseTime == null){
		            	    	minTotalResponseTimeMap.put(String.valueOf(minTimePoint), Integer.parseInt(strReqResponseTime));
		            	    }else{
		            	    	requestResponseTime+=Integer.parseInt(strReqResponseTime);
		            	    	minTotalResponseTimeMap.put(String.valueOf(minTimePoint), requestResponseTime);
		            	    }
	          	    }
				}
		      count++;
		    }
		    
			//String strFilePath = "/tmp/" + System.currentTimeMillis()+".txt";
			//FileUtil.saveContentToFile(sb.toString(), strFilePath);
		    analysisReport.setTPMReport(tpmReport);
			analysisReport.setResponseTimeReport(responseTimeReport);
			System.out.println("tpmReport.size()="+tpmReport.size());
			SortedMap<String, Integer> tmpReport = analysisReport.getTPMReport();
	    	for(Entry<String,Integer> entry : tmpReport.entrySet()){
	    		String key = entry.getKey();
	    		Integer value = entry.getValue();
	    		System.out.println("key="+new Date(Long.parseLong(key))+", value="+value);
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("line count="+count);
		return analysisReport;
	}
	
	private static long getMinTime(long time){
		long sectime = time/1000*1000;
		Date date = new Date(sectime);
		int secs = date.getSeconds();
		long minTime = sectime - secs*1000 ;
		return minTime;
	}
}
