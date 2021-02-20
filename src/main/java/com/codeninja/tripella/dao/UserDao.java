package com.codeninja.tripella.dao;

import org.springframework.data.repository.CrudRepository;

import com.codeninja.tripella.model.User;

public interface UserDao extends CrudRepository<User,Integer>{
	
	public boolean existsByEmailAddress(String emailAddress);
	
	public User findById(int id);
	
	public User findByEmailAddress(String emailAddress);

}
