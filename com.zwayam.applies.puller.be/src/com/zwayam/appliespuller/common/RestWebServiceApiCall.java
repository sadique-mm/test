package com.zwayam.appliespuller.common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;



public class RestWebServiceApiCall {

	static final Logger logger = Logger.getLogger(RestWebServiceApiCall.class);

	public static restResponse executeWebServiceApi(String url,
			String requestMethod,HashMap<String, String> requestProperties,HashMap<String, String> urlParams,String jsonData){
		/*boolean secure=false;
		if(url.toLowerCase().contains("https")){
			secure=true;
		}
		if (secure) {
			return executeHttpsURLConnection(url, 
					 requestMethod,requestProperties,urlParams,jsonData);
		}else 
		{
				return executeHttpURLConnection(url,
						 requestMethod,requestProperties,urlParams,jsonData);
		}*/
		return executeHttpURLConnection(url,
				 requestMethod,requestProperties,urlParams,jsonData);
	}
	
	private static restResponse executeHttpURLConnection(String url,
			String requestMethod,HashMap<String, String> requestProperties,HashMap<String,String> urlParams,String jsonData) {
			int responseCode=500;
			try {
				if(urlParams!=null&&!urlParams.isEmpty()){
					String params = getPostDataString(urlParams);
					url +=params;
				}
				URL u = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) u.openConnection();
				conn.setDoOutput(true);
				switch (requestMethod) {
				case "GET":
					conn.setRequestMethod("GET");
					break;
				case "POST":
					conn.setRequestMethod("POST");
					break;
				case "DELETE":
					conn.setRequestMethod("DELETE");
					break;
				default:
					break;
				}
				if (requestProperties != null && requestProperties.size() > 0) {
					requestProperties.forEach((key, val) -> {
						conn.setRequestProperty(key, val);
					});
				}
				//conn.setRequestProperty("Content-Length", String.valueOf(rawData.length()));
			if (jsonData != null) {
				DataOutputStream os = new DataOutputStream(conn.getOutputStream());
				os.writeBytes(jsonData);
				os.close();
				PrintStream ps = new PrintStream(os);
				ps.print(u.getQuery());
				ps.close();
			}
				String response =null;
				responseCode = conn.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					response = getResponsePayaload(conn.getInputStream());
				}
				return new restResponse(responseCode,"success",response);
			}
	        catch (Exception e) {
				logger.error("Exception while executeHttpURLConnection",e);
				return new restResponse(responseCode,"failed",null);
			}
	}
	
	
	
	private static String getResponsePayaload(InputStream inputStream){
		try {
			StringBuilder jsonString = new StringBuilder();
				BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
				String input;
				while ((input = br.readLine()) != null) {
					jsonString.append(input);
				}
			return jsonString.toString();
		} catch (IOException e) {
			return null;
		}
	}
	

	
	 private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
	        StringBuilder result = new StringBuilder();
	        boolean first = true;
	        for(Map.Entry<String, String> entry : params.entrySet()){
	            if (first){
	            	// result.append("?");
	            	 first = false;
	            } else{
	                result.append("&");
	            }
	            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
	            result.append("=");
	            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
	        }

	        return result.toString();
	    }
	 
	 public static restResponse httpPostAPI(String jobAppUrl, MultipartEntityBuilder builder){
			try {
				HttpEntity entity = builder.build();
				
				HttpClient clientforRead = HttpClients.createDefault();
				HttpPost request = new HttpPost(jobAppUrl);
				request.setEntity(entity);

				HttpResponse response = clientforRead.execute(request);
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				StringBuffer resultBuffer = new StringBuffer();
				String line = "";
				while ((line = reader.readLine()) != null) {
					resultBuffer.append(line);
				}
				return new restResponse(200,"success",resultBuffer.toString());
			} catch (Exception e) {
				logger.error("Exception httpPostAPI",e);
				return new restResponse(500,"failed","");
			}
		}
	
	 
	 
/*	 private static restResponse executeHttpsURLConnection(String url,
				String requestMethod,HashMap<String, String> requestProperties,HashMap<String,String> urlParams,String jsonData) {
			restResponse result =null;
			try {
				if(urlParams!=null&&!urlParams.isEmpty()){
					String params = getPostDataString(urlParams);
					url +=params;
				}
				
				URL u = new URL(url);
				HttpsURLConnection conn = (HttpsURLConnection) u.openConnection();
				conn.setDoOutput(true);
				switch (requestMethod) {
				case "GET":
					conn.setRequestMethod("GET");
					break;
				case "POST":
					conn.setRequestMethod("POST");
					break;
				case "DELETE":
					conn.setRequestMethod("DELETE");
					break;
				default:
					break;
				}
				if (requestProperties != null && requestProperties.size() > 0) {
					requestProperties.forEach((key, val) -> {
						conn.setRequestProperty(key, val);
					});
				}
				//conn.setRequestProperty("Content-Length", String.valueOf(rawData.length()));
				if (jsonData != null) {
					DataOutputStream os = new DataOutputStream(conn.getOutputStream());
					os.writeBytes(jsonData);
				}
				String response =null;
				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					response = getResponsePayaload(conn.getInputStream());
				}
				return new restResponse(conn.getResponseCode(),"success",response);
			} catch (Exception e) {
				logger.error("Exception executeHttpsURLConnection : ",e);
				return result;
			} 
			
		}*/
}