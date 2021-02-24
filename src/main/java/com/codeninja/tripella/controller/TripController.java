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

import com.codeninja.tripella.model.Trip;
import com.codeninja.tripella.service.TripService;

@RestController
public class TripController {

	@Autowired
	TripService tripService;

	@PostMapping("/trip/add")
	public ResponseEntity<?> addTrip(@RequestBody Trip trip) {

			return tripService.addTrip(trip);

	}

	@GetMapping("/trip/index")
	public Iterable<Trip> getTrips() {
		return tripService.getTrips(); 
	}

	@GetMapping("/trip/detail")
	public Trip tripDetails(@RequestParam int id) {

		return tripService.tripDetails(id);

	}

	@PutMapping("/trip/edit")
	public ResponseEntity<?> editTrip(@RequestBody Trip trip) {

			return tripService.editTrip(trip);
	}

	@DeleteMapping("/trip/delete")
	public boolean deleteTrip(@RequestParam int id) {

		return tripService.deleteTrip(id);
	}

}
