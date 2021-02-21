package com.codeninja.tripella.dao;

import org.springframework.data.repository.CrudRepository;


import com.codeninja.tripella.model.Share;


public interface ShareDao extends CrudRepository<Share,Integer>{
		
	public Share findById(int id);

}
