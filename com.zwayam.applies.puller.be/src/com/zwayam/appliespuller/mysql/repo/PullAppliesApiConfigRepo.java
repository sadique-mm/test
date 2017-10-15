package com.zwayam.appliespuller.mysql.repo;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zwayam.appliespuller.mysql.models.PullAppliesApiConfig;



@Repository
public interface PullAppliesApiConfigRepo extends CrudRepository<PullAppliesApiConfig, Integer> {

	PullAppliesApiConfig findByName(String name);   
}
