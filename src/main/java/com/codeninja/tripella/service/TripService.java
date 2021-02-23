package com.codeninja.tripella.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.codeninja.tripella.dao.TripDao;
import com.codeninja.tripella.model.Trip;

public class TripService {

	@Autowired
	TripDao dao;
	
	
	public ResponseEntity<?> addTrip(Trip trip) {
		if(trip.getName() == null)
			return ResponseEntity.badRequest().body("The name can not be void");
		if (trip.getEnd().after(trip.getStart()) || trip.getEnd().equals(trip.getStart()))
			return ResponseEntity.badRequest().body("The date of the end date should be after the start date");

			dao.save(trip);
			return ResponseEntity.ok(trip);

	}
	
	public Iterable<Trip> getTrips() {
		var trips = dao.findAll();
		return trips;
	}

	public Trip tripDetails(int id) {

		Trip trip = dao.findById(id);

		return trip;

	}
	
	public ResponseEntity<?> editTrip(Trip trip) {
		if(trip.getName() == null)
			return ResponseEntity.badRequest().body("The name can not be void");
		if (trip.getEnd().after(trip.getStart()) || trip.getEnd().equals(trip.getStart()))
			return ResponseEntity.badRequest().body("The date of the end date should be after the start date");

			dao.save(trip);
			return ResponseEntity.ok(trip);
	}
	
	public boolean deleteTrip(int id) {

		dao.deleteById(id);
		return true;
	}
}
