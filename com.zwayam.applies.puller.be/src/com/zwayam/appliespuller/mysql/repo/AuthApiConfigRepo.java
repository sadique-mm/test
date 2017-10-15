package com.zwayam.appliespuller.mysql.repo;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zwayam.appliespuller.mysql.models.AuthApiConfig;
import com.zwayam.appliespuller.mysql.models.PullAppliesApiConfig;



@Repository
public interface AuthApiConfigRepo extends CrudRepository<AuthApiConfig, Integer> {

	AuthApiConfig findByName(String name);   
}
