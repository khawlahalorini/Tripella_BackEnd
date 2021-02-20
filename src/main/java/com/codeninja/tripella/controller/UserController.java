package com.codeninja.tripella.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.codeninja.tripella.model.User;
import com.codeninja.tripella.dao.UserDao;

@RestController
public class UserController {
	
	@Autowired
	UserDao userDao;
	
	@PostMapping("/user/register")
	public ResponseEntity<?> register(@RequestBody User user){
		
		if (userDao.existsByEmailAddress(user.getEmailAddress())) {
			return ResponseEntity.ok("User already exists");
		}
		
		BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
		String newPassword = bCrypt.encode(user.getPassword());
		user.setPassword(newPassword);
		
		userDao.save(user);
		return ResponseEntity.ok("registered");
	}
	
}
