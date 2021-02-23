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
import com.codeninja.tripella.config.JwtUtil;
import com.codeninja.tripella.dao.PostDao;
import com.codeninja.tripella.dao.UserDao;

@RestController
public class UserController {

	@Autowired
	UserDao userDao;
	
	@Autowired
	PostDao postDao;

	@PostMapping("/user/register")
	public ResponseEntity<?> register(@RequestBody HashMap<String,String> userData) {
		
		User user = new User(userData);

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

	@GetMapping("/user/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody HashMap<String,String> userData) {

		try {
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(userData.get("emailAddress"), userData.get("password")));
		} catch (BadCredentialsException e) {
			return ResponseEntity.badRequest().body("Incorrect username or password");
		}
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(userData.get("emailAddress"));
		
		return ResponseEntity.ok(jwtUtil.generateToken(userDetails));
	}

	@GetMapping("/user/detail")
	public ResponseEntity<?> getUserDetails(@RequestParam(required = false) Integer id, @AuthenticationPrincipal UserDetailsImpl currentUser) {
		User user = null;
		user = (id !=null && userDao.existsById(id) && currentUser.isAdmin())
					? userDao.findById(id.intValue())
					: userDao.findById(currentUser.getId());
		
		return ResponseEntity.ok(user);
	}

	@PutMapping("/user/update")
	public ResponseEntity<?> updateUser(@RequestParam int id, @RequestBody User newUser){
		
		User oldUser = userDao.findById(newUser.getId());
		
		newUser.setEmailAddress(oldUser.getEmailAddress()); //secure email address
		newUser.setPassword(oldUser.getPassword()); 		//different method for password change
		newUser.setUserRole(oldUser.getUserRole()); 		//secure role; can be changed by another method
		
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
		User user = userDao.findByEmailAddress(email);
		String confirmationToken = UUID.randomUUID().toString();
		user.setConfirmationToken(confirmationToken);
		String resetPasswordLink = "http://localhost:8080/tripella/user/handleresetpassword/resetpassword?token=" + confirmationToken;
        sendEmail(email, resetPasswordLink);
	}
	
	
    public User getByResetPasswordToken(String token) {
        return userDao.findByConfirmationToken(token);
    }
	
    @Autowired
    private JavaMailSender mailSender;
    
    @PostMapping("/user/handleresetpassword/resetpassword/updatepassword")
    public String updatePassword(@RequestParam String newPassword, @RequestParam String token) {
    	User user = userDao.findByConfirmationToken(token);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
         
        user.setConfirmationToken(null);
        userDao.save(user);
        return "succ";
    }
    
    public void sendEmail(String recipientEmail, String link)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();              
        MimeMessageHelper helper = new MimeMessageHelper(message);
         
        helper.setFrom("tt2688519@gmail.com", "Tripella Support");
        helper.setTo(recipientEmail);
         
        String subject = "Here's the link to reset your password";
         
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" +link+ "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";
         
        helper.setSubject(subject);
         
        helper.setText(content, true);
         
        mailSender.send(message);
    }
    
//	@PutMapping("/user/forgotpassword")
//	@PutMapping("/user/updaterole")
	
}
