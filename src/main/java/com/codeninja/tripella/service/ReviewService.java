package com.codeninja.tripella.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codeninja.tripella.dao.ReviewDao;
import com.codeninja.tripella.dao.UserDao;
import com.codeninja.tripella.model.Review;
import com.codeninja.tripella.model.UserDetailsImpl;

@Service
public class ReviewService {

	@Autowired
	ReviewDao reviewDao;
	@Autowired
	UserDao userDao;

	public Review addReview(Review review, UserDetailsImpl currentUser) {
		review.setUser(userDao.findById(currentUser.getId()));
		reviewDao.save(review);

		return review;
	}

	public Iterable<Review> getReviews(UserDetailsImpl currentUser) {
		var reviews = reviewDao.findAllByUser_Id();
		return reviews;
	}

	public Review reviewDetails(int id) {

		Review review = reviewDao.findById(id);

		return review;

	}

	public Review editReview(Review review, UserDetailsImpl currentUser) {
		if(review.getUser().getId() == currentUser.getId() || currentUser.isAdmin())
			reviewDao.save(review);
		
		return review;
	}

	public boolean deleteReview(int id, UserDetailsImpl currentUser) {
		if(reviewDao.findById(id).getUser().getId() == currentUser.getId() || currentUser.isAdmin())
			reviewDao.deleteById(id);
		return true;
	}

}
