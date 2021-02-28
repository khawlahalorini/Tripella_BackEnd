package com.codeninja.tripella.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import javax.mail.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codeninja.tripella.model.User;
import com.codeninja.tripella.model.UserDetailsImpl;
import com.codeninja.tripella.service.ReviewService;
import com.codeninja.tripella.service.TripService;
import com.codeninja.tripella.service.UserService;
import com.codeninja.tripella.service.WishlistService;

@RestController
public class UserController {

	@Autowired
	UserService userService; 
	
	@Autowired
	WishlistService wishlistService;
	
	@Autowired
	TripService tripService; 
	
	@Autowired
	ReviewService reviewService;
	
	@PostMapping("/user/register")
	public ResponseEntity<?> register(@RequestBody HashMap<String,String> userData) throws UnsupportedEncodingException, MessagingException {
		
		return userService.register(userData);
	}

	@GetMapping("/user/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody HashMap<String,String> userData) {
		
		return userService.authenticate(userData); 
	}
	
	@GetMapping("/user/detail")
	public ResponseEntity<?> getUserDetails(@RequestParam(required = false) Integer id, @AuthenticationPrincipal UserDetailsImpl currentUser) {

		return userService.getUserDetails(id,currentUser);
	}

	@PutMapping(value="/user/update", consumes="multipart/form-data")
	public ResponseEntity<?> updateUser(@ModelAttribute User userBody,
			@AuthenticationPrincipal UserDetailsImpl currentUser) throws Exception{
		System.out.println(userBody.shortDetail().toString());
		return userService.updateUser(userBody,currentUser);
	}
	
	@DeleteMapping("/user/delete")
	public ResponseEntity<?> deleteUser(@RequestParam int id, @AuthenticationPrincipal UserDetailsImpl currentUser) {
		
		return userService.deleteUser(id, currentUser);
	}
	
	
	@GetMapping("/user/wishlist")
	public ResponseEntity<?> getWishlist(@AuthenticationPrincipal UserDetailsImpl currentUser) {
		return wishlistService.getWishlist(currentUser);
	}
	
	@PutMapping("/user/wishlist")
	public ResponseEntity<?> addToWishlist(@RequestParam int id,@AuthenticationPrincipal UserDetailsImpl currentUser) {
		return wishlistService.addToWishlist(id, currentUser);
	}
	
	@DeleteMapping("/user/wishlist")
	public ResponseEntity<?> removeFromWishlist(@RequestParam int id,@AuthenticationPrincipal UserDetailsImpl currentUser) {
		return wishlistService.removeFromWishlist(id, currentUser);
	}
	
	@GetMapping("/user/triplist") // ask for service 
	public ResponseEntity<?> getTripList(@AuthenticationPrincipal UserDetailsImpl currentUser) {
		return tripService.getTrips(currentUser);
	}
	
	@GetMapping("/user/reviews") // ask for service 
	public ResponseEntity<?> getReviews(@AuthenticationPrincipal UserDetailsImpl currentUser) {
		return ResponseEntity.ok(reviewService.getReviews(currentUser));
	}
	
	@PutMapping("/user/forgotpassword")
	public void handleresetpassword(@RequestParam String email) throws UnsupportedEncodingException, MessagingException {
		userService.handleresetpassword(email);
	}
    
	
    @PostMapping("/user/forgotpassword/resetpassword/updatepassword") 
    public void updatePassword(@RequestParam String newPassword, @RequestParam String token) {
    	userService.updatePassword(newPassword, token);
    }
    

    @PostMapping("/user/active")
    public void updateActive(@RequestParam String email) {
    	userService.updateActive(email);
    }

	@PutMapping("/user/updaterole")
	public ResponseEntity<?> updateUserRole(@RequestBody HashMap<String,String> userData,@AuthenticationPrincipal UserDetailsImpl currentUser){
		return userService.updateUserRole(currentUser,userData);
	}
}
