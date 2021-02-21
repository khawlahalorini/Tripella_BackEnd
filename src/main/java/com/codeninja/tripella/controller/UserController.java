package com.codeninja.tripella.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.codeninja.tripella.model.JwtResponse;
import com.codeninja.tripella.model.User;
import com.codeninja.tripella.config.JwtUtil;
import com.codeninja.tripella.dao.UserDao;

@RestController
public class UserController {

	@Autowired
	UserDao userDao;

	@PostMapping("/user/register")
	public ResponseEntity<?> register(@RequestBody User user) {

		if (userDao.existsByEmailAddress(user.getEmailAddress())) {
			return ResponseEntity.ok("User already exists");
		}

		BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
		String newPassword = bCrypt.encode(user.getPassword());
		user.setPassword(newPassword);

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
			return ResponseEntity.ok("Incorrect username or password");
		}

		UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmailAddress());
		
		return ResponseEntity.ok(new JwtResponse(jwtUtil.generateToken(userDetails)));
	}

}
