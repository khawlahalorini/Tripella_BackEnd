package com.codeninja.tripella.service;


import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.codeninja.tripella.dao.DetailDao;
import com.codeninja.tripella.dao.PostDao;
import com.codeninja.tripella.dao.TripDao;
import com.codeninja.tripella.model.Detail;
import com.codeninja.tripella.model.Post;
import com.codeninja.tripella.model.Trip;

@Service
public class DetailService {
	
	@Autowired
	PostDao postDao; 
	
	@Autowired
	DetailDao detailDao;
	
	@Autowired
	TripDao tripDao; 
	
	public ResponseEntity<?> addDetail(HashMap<String, String> detail) {

		try {
			Trip trip = tripDao.findById(Integer.parseInt(detail.get("trip_id")));
			Post post = postDao.findById(Integer.parseInt(detail.get("post_id")));
			Detail detail1 = new Detail();
			detail1.setTrip(trip);
			detail1.setPost(post);
			detail1.setDateTime(Date.valueOf(detail.get("dateTime")));
			detailDao.save(detail1);
			return ResponseEntity.ok("Added");
		}
		catch (Exception e){
			System.out.println(e);
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
