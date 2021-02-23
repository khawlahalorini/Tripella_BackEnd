package com.codeninja.tripella.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.mail.*;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codeninja.tripella.model.Post;
import com.codeninja.tripella.model.User;
import com.codeninja.tripella.model.UserDetailsImpl;
import com.codeninja.tripella.service.UserService;
import com.codeninja.tripella.config.JwtUtil;
import com.codeninja.tripella.dao.PostDao;
import com.codeninja.tripella.dao.UserDao;

@RestController
public class UserController {

	@Autowired
	UserService userService; 
	
	@Autowired
	PostDao postDao;

	@PostMapping("/user/register")
	public ResponseEntity<?> register(@RequestBody HashMap<String,String> userData) {
		
		return userService.register(userData);
	}

	@GetMapping("/user/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody HashMap<String,String> userData) {
		
		return userService.authenticate(userData); 
	}

	@PutMapping("/user/update")
	public ResponseEntity<?> updateUser(@RequestParam int id, @RequestBody User newUser){
		
		return userService.updateUser(id, newUser);

	}
	
	@DeleteMapping("/user/delete")
	public ResponseEntity<?> deleteUser(@RequestParam int id, @AuthenticationPrincipal UserDetailsImpl currentUser) {
		
		return userService.deleteUser(id, currentUser);
	}
	
	
	@GetMapping("/user/wishlist")
	public ResponseEntity<?> getWishlist(@AuthenticationPrincipal UserDetailsImpl currentUser) {
		User user = userDao.findById(currentUser.getId());
//		return ResponseEntity.ok(user.getWishlist());
		return ResponseEntity.ok(postDao.findAllByWishlistedBy_Id(user.getId()));
	}
	
	@PutMapping("/user/wishlist")
	public ResponseEntity<?> addToWishlist(@RequestParam int id,@AuthenticationPrincipal UserDetailsImpl currentUser) {
		
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
	
	@DeleteMapping("/user/wishlist")
	public ResponseEntity<?> removeFromWishlist(@RequestParam int id,@AuthenticationPrincipal UserDetailsImpl currentUser) {
		//List<Post> whishlist
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/user/triplist")
	public ResponseEntity<?> getTripList(@AuthenticationPrincipal UserDetailsImpl currentUser) {
		User user = userDao.findById(currentUser.getId());
		return ResponseEntity.ok(user.getTrip());
	}
	
	@PutMapping("/user/handleresetpassword")
	public void handleresetpassword(@RequestParam String email) throws UnsupportedEncodingException, MessagingException {
		userService.handleresetpassword(email);
	}
    
	
    @PostMapping("/user/handleresetpassword/resetpassword/updatepassword")
    public void updatePassword(@RequestParam String newPassword, @RequestParam String token) {
    	userService.updatePassword(newPassword, token);
    }
    
//	@PutMapping("/user/forgotpassword")
//	@PutMapping("/user/updaterole")
	
}
