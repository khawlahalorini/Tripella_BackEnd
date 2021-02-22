package com.codeninja.tripella.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codeninja.tripella.dao.ReviewDao;
import com.codeninja.tripella.model.Review;

@RestController
public class ReviewController {

	@Autowired
	ReviewDao dao;

	@PostMapping("/review/add")
	public Review addReview(@RequestBody Review review) {
		dao.save(review);

		return review;
	}

	@GetMapping("/review/index")
	public Iterable<Review> getReviews() {
		var reviews = dao.findAll();
		return reviews;
	}

	@GetMapping("/review/detail")
	public Review reviewDetails(@RequestParam int id) {

		Review review = dao.findById(id);

		return review;

	}

	@PutMapping("/review/edit")
	public Review editReview(@RequestBody Review review) {
		dao.save(review);

		return review;
	}

	@DeleteMapping("/review/delete")
	public boolean deleteReview(@RequestParam int id) {

		dao.deleteById(id);
		return true;
	}

}
