package com.zwayam.appliespuller.common;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class FileUtils {
	public static String getFileNameFromURL(String fileUrl) throws IOException{
		String fileName=null;
		String ext="";
		if(fileUrl!=null&&!fileUrl.isEmpty()&&!fileUrl.equalsIgnoreCase("null")){
			fileUrl=fileUrl.toLowerCase();
			String temp ="";
			int fileIndex = fileUrl.indexOf("filename=");
			int extIndex = fileUrl.indexOf("ext=");
			if(fileIndex>0){
				temp = fileUrl.substring(fileIndex+9,fileUrl.length());
				if(temp.indexOf("&")>0)
				{temp=temp.substring(0,temp.indexOf("&"));}
			}
			fileName=temp;
			temp="";
			if(extIndex>0){
				temp = fileUrl.substring(extIndex+4,fileUrl.length());
				if(temp.indexOf("&")>0)
				{temp=temp.substring(0,temp.indexOf("&"));}
			}
			ext=temp;
		//second method
		/*	if(fileUrl!=null&&!fileUrl.equalsIgnoreCase("null")&&!fileUrl.trim().isEmpty()){
				
				  final URL url = new URL(fileUrl);  
			      final URLConnection connection = url.openConnection();  
			      String fileType = connection.getContentType();  
			      ext = getExtenFromContentType(fileType);
			      fileName="temp_resume"+"."+ext;
			}*/
			fileName=fileName+"."+ext;
		}
		return fileName;
	}
	public static String getFileNameFromURL(String fileUrl,String name) throws IOException{
		String fileName=null;
		if(fileUrl!=null&&!fileUrl.isEmpty()&&!fileUrl.equalsIgnoreCase("null")){
		 fileUrl=fileUrl.toLowerCase();
		 String temp="";
		 int fileIndex = fileUrl.indexOf("candidate=");
		 if(fileIndex>0){
				temp = fileUrl.substring(fileIndex+10,fileUrl.length());
		 }
		 fileName=temp;
		}
		 return fileName;
		 
		}
	

	
	
	private static String getExtenFromContentType(String contentType){
		String ext = "";
		contentType= contentType.toLowerCase();
		HashMap<String,String> hm = new HashMap<>();
		hm.put("application/msword","doc");
		hm.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document","docx");
		hm.put("application/pdf","pdf");
		hm.put("text/plain","txt");
		hm.put("image/png","png");
		hm.put("image/jpeg","jpg");
		hm.put("application/xml","xml");
		hm.put("application/vnd.ms-powerpoint","ppt");
		
		ext=hm.get(contentType);
	
		return ext;
	}
}
