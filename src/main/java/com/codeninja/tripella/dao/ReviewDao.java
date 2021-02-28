package com.codeninja.tripella.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.codeninja.tripella.model.Review;


public interface ReviewDao extends CrudRepository<Review,Integer>{
		
	public Review findById(int id);
	
	public List<Review> findAllByPost_Id(int id);

	public Long countByPost_Id(int id);

}
