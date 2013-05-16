package com.lt.loadtest.loadtestagent.infrastructure.common.file;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class FileUtil {
	private static final Log logger = LogFactory.getLog(FileUtil.class);
    
	public static void saveContentToFile(String strContent, String strFilePath) throws Exception{
		try {
			byte[] bytes=strContent.getBytes();
			FileOutputStream fos = new FileOutputStream(strFilePath);
			fos.write(bytes);
			fos.close();
		} catch (FileNotFoundException e) {
			logger.error(e);
			throw e;
		} catch (IOException e) {
			logger.error(e);
			throw e;
		}
	}
}
