package com.codeninja.tripella.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.codeninja.tripella.model.Post;


public interface PostDao extends CrudRepository<Post,Integer>{
		
	public Post findById(int id);
	
	List<Post> findByTitle(String title);

	List<Post> findAllByCity(String city);

	List<Post> findAllByCountry(String country);
	
	List<Post> findAllByType(String type);
	
	List<Post> findAllByWishlistedBy_Id(int id);

}
