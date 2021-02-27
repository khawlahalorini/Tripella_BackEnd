package com.codeninja.tripella.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.codeninja.tripella.dao.PostDao;
import com.codeninja.tripella.dao.UserDao;
import com.codeninja.tripella.model.Post;
import com.codeninja.tripella.model.User;
import com.codeninja.tripella.model.UserDetailsImpl;

@Service
public class WishlistService {

	@Autowired
	UserDao userDao;
	
	@Autowired
	PostDao postDao; 
	
	
	public ResponseEntity<?> getWishlist(UserDetailsImpl currentUser) {
		User user = userDao.findById(currentUser.getId());
//		return ResponseEntity.ok(user.getWishlist());
		return ResponseEntity.ok(postDao.findAllByWishlistedBy_Id(user.getId()));
	}
	
	public ResponseEntity<?> addToWishlist(int id,UserDetailsImpl currentUser) {
		
		//get user object & user's wishlist
		User user = userDao.findById(currentUser.getId());
		List<Post> wishlist = user.getWishlist();
		Post post;
		
		//validate post id
		if(!postDao.existsById(id)) 
			return ResponseEntity.badRequest().build();
		
		post = postDao.findById(id);
		
		if ( !wishlist.contains( postDao.findById(id) ) ) {
			wishlist.add(postDao.findById(id));
			user.setWishlist(wishlist);
			userDao.save(user);
		}
		
		return ResponseEntity.ok().build();
	}
	
	public ResponseEntity<?> getTripList(UserDetailsImpl currentUser) {
		User user = userDao.findById(currentUser.getId());
		return ResponseEntity.ok(user.getTrip());
	}
	
	
}
