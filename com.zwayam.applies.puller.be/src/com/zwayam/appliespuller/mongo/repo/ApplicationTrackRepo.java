package com.zwayam.appliespuller.mongo.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.zwayam.appliespuller.mongo.models.JobApplication;


public interface ApplicationTrackRepo extends MongoRepository<JobApplication,String> {
	List<JobApplication> findBySourceAndCompanyIdAndJobIdAndEmailId(String jobBoardName,String companyId,String jobId,String emailId);
}
