package com.codeninja.tripella.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.codeninja.tripella.model.Detail;


public interface DetailDao extends CrudRepository<Detail,Integer>{
		
	public Detail findById(int id);
	
	List<Detail> findAllByTrip_id(int id);

}
