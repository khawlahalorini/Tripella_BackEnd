package com.codeninja.tripella.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codeninja.tripella.model.Post;
import com.codeninja.tripella.model.User;
import com.codeninja.tripella.model.UserDetailsImpl;
import com.codeninja.tripella.config.JwtUtil;
import com.codeninja.tripella.dao.UserDao;

@RestController
public class UserController {

	@Autowired
	UserDao userDao;

	@PostMapping("/user/register")
	public ResponseEntity<?> register(@RequestBody User user) {

		if (userDao.existsByEmailAddress(user.getEmailAddress())) {
			return ResponseEntity.badRequest().body("User already exists");
		}

		BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
		String newPassword = bCrypt.encode(user.getPassword());
		user.setPassword(newPassword);
		
		// secure user roles
//		user.setUserRole("ROLE_USER");
		
		userDao.save(user);
		
		return ResponseEntity.ok("registered");
	}

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	UserDetailsService userDetailsService;

	@PostMapping("/user/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody User user) {

		try {
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getEmailAddress(), user.getPassword()));
		} catch (BadCredentialsException e) {
			return ResponseEntity.badRequest().body("Incorrect username or password");
		}

		UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmailAddress());
		
		return ResponseEntity.ok(jwtUtil.generateToken(userDetails));
	}

	@PostMapping("/user/update")
	public ResponseEntity<?> updateUser(@RequestParam int id, @RequestBody User newUser){
		
		User oldUser = userDao.findById(newUser.getId());
		
		newUser.setEmailAddress(oldUser.getEmailAddress()); //secure email address
		newUser.setPassword(oldUser.getPassword()); 		//different method for password change
		newUser.setUserRole("ROLE_USER"); 					//secure role; can be changed by another method
		
		try {
			userDao.save(newUser);
			return ResponseEntity.ok("Updated");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Not Updated");
		}

	}
	
	@DeleteMapping("/user/delete")
	public ResponseEntity<?> deleteUser(@RequestParam int id, @AuthenticationPrincipal UserDetailsImpl currentUser) {
		
		if (currentUser.isAdmin() && userDao.existsById(id) && id != currentUser.getId()) {
			userDao.deleteById(id);
			return ResponseEntity.ok("Deleted");
		}
		
		return ResponseEntity.badRequest().build();
	}
	
	
	
	@GetMapping("/user/wishlist")
	public ResponseEntity<?> getWishlist(@AuthenticationPrincipal UserDetailsImpl currentUser) {
		User user = userDao.findById(currentUser.getId());
		return ResponseEntity.ok(user.getWishlist());
	}
	
	@GetMapping("/user/triplist")
	public ResponseEntity<?> getTripList(@AuthenticationPrincipal UserDetailsImpl currentUser) {
		User user = userDao.findById(currentUser.getId());
		return ResponseEntity.ok(user.getTrip());
	}
	
	@GetMapping("/user/detail")
	public ResponseEntity<?> getUserDetails(@RequestParam int id, @AuthenticationPrincipal UserDetailsImpl currentUser) {
		User user = null;
		user = (userDao.existsById(id) && currentUser.isAdmin())
					? userDao.findById(id)
					: userDao.findById(currentUser.getId());
		
		user.setPassword("**********"); // hide password 
		return ResponseEntity.ok(user);
	}
	
//	@GetMapping("/user/changepassword")
//	@GetMapping("/user/forgotpassword")
//	@GetMapping("/user/updaterole")
}
