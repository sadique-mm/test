package com.zwayam.appliespuller.controller;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zwayam.appliespuller.be.PullAppliesComponent;

@RestController
@RequestMapping("/pullappliesAPI")
public class AppliesPullerController {
	
	@Autowired
	PullAppliesComponent component;
	
	@RequestMapping(value = "/pull", method = RequestMethod.GET)
	public @ResponseBody String pull() throws IOException{
		component.cronJobForPullingApplies();
		//component.testD();
		return "pulled";
	}
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public @ResponseBody Object test(
			@RequestParam(value = "jobId") String jobId,
			@RequestParam(value = "companyId") String companyId
			,@RequestParam(value = "client") String client) throws ParseException{
		return "{\"Status\": \"Success\",\"HTTPStatusCode\": \"OK\",\"TotalApplications\": 2,\"ApplicationList\": [{\"name\": \"Rohini Patil\",\"email\": \"rohinipatil36@gmail.com\",\"phone\": \"9898989896\",\"experience\": \"4\",\"currentLocation\": \"bengalur\",\"dob\": \"04/02/1991\",\"expectedCTC\": \"8.0\",\"preferredLocations\": \"bengaluru, delhi,chennai\",\"resumeURL\":\"http://stagemnp.com/downloadS3Object.rest?keyId=resumes/1483945664609_686817.pdf&fileName=Rohini Patil_Hiree_Profile&ext=pdf\"}]}";
	}

}
