package com.codeninja.tripella.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codeninja.tripella.model.Review;
import com.codeninja.tripella.model.UserDetailsImpl;
import com.codeninja.tripella.service.ReviewService;

@RestController
public class ReviewController {

	@Autowired
	ReviewService reviewService;
	

	@PostMapping("/review/add")
	public Review addReview(@RequestBody Review review, @AuthenticationPrincipal UserDetailsImpl currentUser) {
		return reviewService.addReview(review, currentUser);
	}

	@GetMapping("/review/detail")
	public Review reviewDetails(@RequestParam int id) {

		return reviewService.reviewDetails(id);

	}

	@PutMapping("/review/edit")
	public Review editReview(@RequestBody Review review, @AuthenticationPrincipal UserDetailsImpl currentUser) {
		return reviewService.editReview(review, currentUser);
	}

	@DeleteMapping("/review/delete")
	public boolean deleteReview(@RequestParam int id, @AuthenticationPrincipal UserDetailsImpl currentUser) {
		return reviewService.deleteReview(id, currentUser);
	}

}
