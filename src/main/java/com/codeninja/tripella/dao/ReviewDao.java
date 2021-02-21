package com.codeninja.tripella.dao;

import org.springframework.data.repository.CrudRepository;

import com.codeninja.tripella.model.Review;


public interface ReviewDao extends CrudRepository<Review,Integer>{
		
	public Review findById(int id);
	


}
