package com.codeninja.tripella.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codeninja.tripella.model.Trip;
import com.codeninja.tripella.model.UserDetailsImpl;
import com.codeninja.tripella.service.TripService;

@RestController
public class TripController {

	@Autowired
	TripService tripService;

	@PostMapping("/trip/add")
	public ResponseEntity<?> addTrip(@RequestBody Trip trip,@AuthenticationPrincipal UserDetailsImpl currentUser){

			return tripService.addTrip(trip,currentUser);

	}

	@GetMapping("/trip/detail")
	public Trip tripDetails(@RequestParam int id, @AuthenticationPrincipal UserDetailsImpl currentUser) {

		return tripService.tripDetails(id, currentUser);

	}

	@PutMapping("/trip/edit")
	public ResponseEntity<?> editTrip(@RequestBody Trip trip,@AuthenticationPrincipal UserDetailsImpl currentUser) {

			return tripService.editTrip(trip, currentUser);
	}

	@DeleteMapping("/trip/delete")
	public boolean deleteTrip(@RequestParam int id, @AuthenticationPrincipal UserDetailsImpl currentUser) {

		return tripService.deleteTrip(id, currentUser);
	}
	
	@GetMapping("/user/triplist") // ask for service 
	public ResponseEntity<?> getTripList(@AuthenticationPrincipal UserDetailsImpl currentUser) {
		return tripService.getTrips(currentUser);
	}

}
