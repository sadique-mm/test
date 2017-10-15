package com.zwayam.appliespuller.mongo.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JobApplication {

	@JsonIgnore
	public String id;
	
	public String jobBoardName;
	
	public String applicantId;
    
	public String phoneNo;
	
	public String emailId;
	
	public String firstName;
	
	public String middleName;
	
	public String lastName;
	
	public Date date;
	
	public String jobId;
    
	public String companyId;
	
	public String fileName;
	
    public String resumeURL;
    
    public String experience;
    
    public String expectedSalary;
    
    public String location;
    
    public String dateOfBirth;
    
    public String source;
    
    public Object resume;

}
