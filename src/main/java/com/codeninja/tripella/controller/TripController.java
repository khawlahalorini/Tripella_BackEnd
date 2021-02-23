package com.codeninja.tripella.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codeninja.tripella.dao.TripDao;
import com.codeninja.tripella.model.Trip;

@RestController
public class TripController {

	@Autowired
	TripDao dao;

	@PostMapping("/trip/add")
	public ResponseEntity<?> addTrip(@RequestBody Trip trip) {
		if(trip.getName() == null)
			return ResponseEntity.badRequest().body("The name can not be void");
		if (trip.getEnd().after(trip.getStart()) || trip.getEnd().equals(trip.getStart()))
			return ResponseEntity.badRequest().body("The date of the end date should be after the start date");

			dao.save(trip);
			return ResponseEntity.ok(trip);

	}

	@GetMapping("/trip/index")
	public Iterable<Trip> getTrips() {
		var trips = dao.findAll();
		return trips;
	}

	@GetMapping("/trip/detail")
	public Trip tripDetails(@RequestParam int id) {

		Trip trip = dao.findById(id);

		return trip;

	}

	@PutMapping("/trip/edit")
	public ResponseEntity<?> editTrip(@RequestBody Trip trip) {
		if(trip.getName() == null)
			return ResponseEntity.badRequest().body("The name can not be void");
		if (trip.getEnd().after(trip.getStart()) || trip.getEnd().equals(trip.getStart()))
			return ResponseEntity.badRequest().body("The date of the end date should be after the start date");

			dao.save(trip);
			return ResponseEntity.ok(trip);
	}

	@DeleteMapping("/trip/delete")
	public boolean deleteTrip(@RequestParam int id) {

		dao.deleteById(id);
		return true;
	}

}
