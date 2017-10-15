package com.zwayam.appliespuller.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;

public class HttpDownloadUtility {
	
	static final Logger logger = Logger.getLogger(HttpDownloadUtility.class);

    private static final int BUFFER_SIZE = 4096;

    public static boolean downloadFile(String fileURL, String saveDir,String fileName)
            throws IOException {
        URL url = new URL(fileURL);
        HttpsURLConnection httpConn = (HttpsURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();
        logger.error("downloadFile response : " +responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            
            InputStream inputStream = httpConn.getInputStream();
            String saveFilePath = saveDir + File.separator + fileName;
             
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);
 
            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
 
            outputStream.close();
            inputStream.close();
 
            logger.error("AssessmentResult File downloaded"+fileName);
        } else {
        	logger.error("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
		return true;
    }
    
   
}