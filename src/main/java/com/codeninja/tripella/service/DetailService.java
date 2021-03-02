package com.codeninja.tripella.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.codeninja.tripella.dao.DetailDao;
import com.codeninja.tripella.dao.TripDao;
import com.codeninja.tripella.model.Detail;
import com.codeninja.tripella.model.Post;
import com.codeninja.tripella.model.Trip;

@Service
public class DetailService {
	
	@Autowired
	DetailDao detailDao;
	
	@Autowired
	TripDao tripDao; 
	
	public ResponseEntity<?> addDetail(Detail detail) {

		try {
			detailDao.save(detail);
			return ResponseEntity.ok("Added");
		}
		catch (Exception e){
			return ResponseEntity.badRequest().body(e);
		}
		
	}

	public ResponseEntity<?> editDetail(Detail detail) {
			if(detailDao.findById(detail.getId()) != null) {
				detailDao.save(detail);
				return ResponseEntity.ok("updated");
			}
			return ResponseEntity.badRequest().body("not found");
	}

	public ResponseEntity<?> detailDetails(int id) {
		return detailDao.findById(id) == null
				? ResponseEntity.badRequest().body("Not updated")
				: ResponseEntity.accepted().body(detailDao.findById(id));
	}
}
