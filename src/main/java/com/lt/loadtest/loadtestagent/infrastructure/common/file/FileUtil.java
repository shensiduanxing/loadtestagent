package com.lt.loadtest.loadtestagent.infrastructure.common.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

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
	
	public static char getPosChar(File file, long pos){
		FileInputStream fis = null;
		FileChannel fc = null;
		long fileSize = 0;
		char ch = '1';
		try {
			fis = new FileInputStream (file);
			fc = fis.getChannel();
			fileSize = fc.size();
			int BACKOFF_COUNT = 1024;
			
			MappedByteBuffer filedata = fc.map (MapMode.READ_ONLY, 0, fileSize);

			byte[] buffer = new byte[BACKOFF_COUNT];
			int anchor = 0;
			while(filedata.hasRemaining() && anchor<pos){
				int len = buffer.length;
				if(filedata.remaining() < BACKOFF_COUNT){
					len = filedata.remaining();
				}
				filedata.get(buffer, 0, len);
				for(int i=0;i<len;i++){
					if(anchor==pos){
						ch = (char) buffer[i];
					    break;
					}
					anchor++;
				}
			}
			return ch;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 'a';
	}
	
	public static String getAnalyzableLines(File file, long startPos, long endPos){
        byte[] buffer = new byte[1024];
        FileInputStream fis = null;
		FileChannel fc = null;
		long fileSize = 0;
		long lastEofPos = 0;
		
		StringBuilder sb = new StringBuilder();
		try {
			fis = new FileInputStream (file);
			fc = fis.getChannel();
			fileSize = fc.size();
			
			if (startPos<endPos && (endPos<= fileSize)){
				lastEofPos = getLastEOFPos(file, startPos, endPos-startPos);
				System.out.println("lastEofPos="+lastEofPos);
				long size = lastEofPos - startPos;
				if(size>0){
					MappedByteBuffer filedata = fc.map (MapMode.READ_ONLY, startPos, size);
					
					while(filedata.hasRemaining()){
						int len = buffer.length;
						if(filedata.remaining() < 1024){
							len = filedata.remaining();
						}
						filedata.get(buffer, 0, len);
						String str = new String(buffer);
						sb.append(str);
					}
				}
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return sb.toString();
	}
	
	public static long getLastEOFPos(File file, long startPos, long size){
		//compute the last EOF between startPos and startPos+Size
		long lastEOFPos = 0L;
		FileInputStream fis = null;
		FileChannel fc = null;
		long fileSize = 0;
		
		try {
			fis = new FileInputStream (file);
			fc = fis.getChannel();
			fileSize = fc.size();
			int BACKOFF_COUNT = 1024;
			long mapStartPos = startPos;
			long mapSize = size;
			if(size > BACKOFF_COUNT){
			    mapStartPos = startPos + size - BACKOFF_COUNT;
			    mapSize = BACKOFF_COUNT;
			}

			MappedByteBuffer filedata = fc.map (MapMode.READ_ONLY, mapStartPos, mapSize);

			byte[] buffer = new byte[BACKOFF_COUNT];
			while(filedata.hasRemaining()){
				int len = buffer.length;
				if(filedata.remaining() < BACKOFF_COUNT){
					len = filedata.remaining();
				}
				filedata.get(buffer, 0, len);
				for(int i=len-1;i>0;i--){
					char c = (char)buffer[i];
					//System.out.println("==========i="+i+"==ch="+c);
					if(c == '\n'){
						lastEOFPos = mapStartPos + i;
						System.out.println("lastEOFPosTmp="+lastEOFPos);
						if(lastEOFPos!=(mapStartPos+mapSize-1)){
							System.out.println("mapStartPos+mapSize="+(mapStartPos+mapSize));
							System.out.println("==========lastEOFPos="+lastEOFPos+" is EOF");
						    break;
						}
					}
				}
			}
		}catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return lastEOFPos;
	}
}
