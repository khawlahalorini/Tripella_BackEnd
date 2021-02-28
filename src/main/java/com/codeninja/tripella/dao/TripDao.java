package com.codeninja.tripella.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.codeninja.tripella.model.Trip;


public interface TripDao extends CrudRepository<Trip,Integer>{
		
	public Trip findById(int id);

	public List<Trip> findAllByUser_Id(int id);

	public List<Trip> findAllBysharedWith_Id(int id);
}
