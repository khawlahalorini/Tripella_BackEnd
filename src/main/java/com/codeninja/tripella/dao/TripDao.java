package com.codeninja.tripella.dao;

import org.springframework.data.repository.CrudRepository;

import com.codeninja.tripella.model.Trip;


public interface TripDao extends CrudRepository<Trip,Integer>{
		
	public Trip findById(int id);


}
