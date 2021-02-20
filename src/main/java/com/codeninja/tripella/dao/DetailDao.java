package com.codeninja.tripella.dao;

import org.springframework.data.repository.CrudRepository;

import com.codeninja.tripella.model.Detail;


public interface DetailDao extends CrudRepository<Detail,Integer>{
		
	public Detail findById(int id);
	


}
