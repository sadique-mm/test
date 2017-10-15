package com.zwayam.appliespuller.common;

//import org.apache.log4j.Logger;


public class restResponse {
	
	//static final Logger logger = Logger.getLogger(restResponse.class);

		public int status;
	   
		public String message;
	    
		public Object  result;
	    
		public Exception exception;
		
		public String duration;
	    
		public restResponse(String theMessage,Object theResult){
			this.status = 200;
			this.message = theMessage;
			this.result = theResult;
		}
		
		public restResponse(int status ,String theMessage,Object theResult){
			this.status = 200;
			this.message = theMessage;
			this.result = theResult;
		}
		
		public restResponse(String theMessage,Object theResult,String duration){
			this.status = 200;
			this.message = theMessage;
			this.result = theResult;
			this.duration = duration;
		}
		
		public restResponse(int theErrorCode,String theErrorMessage,Object theResult, Exception theException){
			this.status = theErrorCode;
			this.message = theErrorMessage;
			this.result = theResult;
			this.exception = theException;	
			
		}

		public restResponse(int status, String message) {
			this.status = status;
			this.message = message;
		}
	}
