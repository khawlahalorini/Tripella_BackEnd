package com.codeninja.tripella.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.codeninja.tripella.dao.TripDao;
import com.codeninja.tripella.dao.UserDao;
import com.codeninja.tripella.model.Trip;
import com.codeninja.tripella.model.User;
import com.codeninja.tripella.model.UserDetailsImpl;

@Service
public class TripService {

	@Autowired
	TripDao dao;
	
	@Autowired
	UserDao userDao;
	
	
	public ResponseEntity<?> addTrip(Trip trip,UserDetailsImpl currentUser) {
		if(trip.getName() == null)
			return ResponseEntity.badRequest().body("The name can not be void");
		if (trip.getStart() == null || trip.getEnd() == null || !trip.getEnd().after(trip.getStart()))
			return ResponseEntity.badRequest().body("The date of the end date should be after the start date");
			
			trip.setUser(userDao.findById(currentUser.getId()));
			dao.save(trip);
			return ResponseEntity.ok(trip);

	}
	
	public ResponseEntity<?> getTrips(UserDetailsImpl currentUser) {
		List<Trip> trips = dao.findAllByUser_Id(currentUser.getId());
		trips.addAll(dao.findAllBysharedWith_Id(currentUser.getId()));
		return ResponseEntity.ok(trips);
	}

	public Trip tripDetails(int id, UserDetailsImpl currentUser) {
		User user = userDao.findById(id);
		Trip trip = dao.findById(id);
		if(trip.getUser() == user || trip.getSharedWith().contains(user))
			return trip;
		return null;
	}
	
	public ResponseEntity<?> editTrip(Trip trip, UserDetailsImpl currentUser) {
		if(trip.getUser().getId() != currentUser.getId())
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		if(trip.getName() == null)
			return ResponseEntity.badRequest().body("The name can not be void");
		if (trip.getEnd().after(trip.getStart()) || trip.getEnd().equals(trip.getStart()))
			return ResponseEntity.badRequest().body("The date of the end date should be after the start date");

			dao.save(trip);
			return ResponseEntity.ok(trip);
	}
	
	public boolean deleteTrip(int id, UserDetailsImpl currentUser) {
		if(dao.findById(id).getUser().getId() == currentUser.getId()){
			dao.deleteById(id);
		return true;
		}
		return false;
	}
}
