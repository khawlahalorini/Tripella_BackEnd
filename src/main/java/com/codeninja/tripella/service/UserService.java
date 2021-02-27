package com.codeninja.tripella.service;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.codeninja.tripella.config.JwtUtil;
import com.codeninja.tripella.dao.UserDao;
import com.codeninja.tripella.model.User;
import com.codeninja.tripella.model.UserDetailsImpl;

@Service
public class UserService {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	UserDao userDao;

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	private JavaMailSender mailSender;

	public ResponseEntity<?> register(HashMap<String, String> userData) throws UnsupportedEncodingException, MessagingException {

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
		String activeLink = "http://localhost:8080/tripella/user/active?email="
				+ user.getEmailAddress();
		sendEmailForActive(user.getEmailAddress(),activeLink );
		return ResponseEntity.ok("registered, check your email to active your account");
	}

	public ResponseEntity<?> authenticate(HashMap<String, String> userData) {

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(userData.get("emailAddress"), userData.get("password")));
		} catch (BadCredentialsException e) {
			return ResponseEntity.badRequest().body("Incorrect username or password");
		}

		UserDetails userDetails = userDetailsService.loadUserByUsername(userData.get("emailAddress"));

		return ResponseEntity.ok(jwtUtil.generateToken(userDetails));
	}

	public ResponseEntity<?> updateUser(int id, User newUser) {

		User oldUser = userDao.findById(newUser.getId());

		newUser.setEmailAddress(oldUser.getEmailAddress()); // secure email address
		newUser.setPassword(oldUser.getPassword()); // different method for password change
		newUser.setUserRole(oldUser.getUserRole()); // secure role; can be changed by another method

		try {
			userDao.save(newUser);
			return ResponseEntity.ok("Updated");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Not Updated");
		}

	}

	public ResponseEntity<?> deleteUser(int id, UserDetailsImpl currentUser) {

		if (currentUser.isAdmin() && userDao.existsById(id) && id != currentUser.getId()) {
			userDao.deleteById(id);
			return ResponseEntity.ok("Deleted");
		}

		return ResponseEntity.badRequest().build();
	}

	public void handleresetpassword(String email) throws UnsupportedEncodingException, MessagingException {
		
		// adding if user find (if....else or try.....catch? ) 
		User user = userDao.findByEmailAddress(email);
		String confirmationToken = UUID.randomUUID().toString();
		user.setConfirmationToken(confirmationToken);
		userDao.save(user);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		System.out.println("Time Stamp" + timestamp);
		Date date= new Date(timestamp.getTime());
		user.setExpiryDate(date);
		String resetPasswordLink = "http://localhost:8080/tripella/user/handleresetpassword/resetpassword?token="
				+ confirmationToken; // change this in deployment 
		sendEmail(email, resetPasswordLink);
	}

	public String updatePassword(String newPassword, String token) {
		User user = userDao.findByConfirmationToken(token);
		System.out.println("token is " + user.getConfirmationToken());
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(newPassword);
		user.setPassword(encodedPassword);
		user.setConfirmationToken(null);
		userDao.save(user);
		return "succ";
	}

	void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("tt2688519@gmail.com", "Tripella Support");
		helper.setTo(recipientEmail);

		String subject = "Here's the link to reset your password";

		String content = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>"
				+ "<p>Click the link below to change your password:</p>" + "<p><a href=\"" + link
				+ "\">Change my password</a></p>" + "<br>" + "<p>Ignore this email if you do remember your password, "
				+ "or you have not made the request.</p>";

		helper.setSubject(subject);

		helper.setText(content, true);

		mailSender.send(message);
	}
	
	
	void sendEmailForActive(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("tt2688519@gmail.com", "Tripella Support");
		helper.setTo(recipientEmail);

		String subject = "Here's the link to active your account";

		String content = "<p>Hello,</p>"
				+ "<p>Click the link below to acctive your account:</p>" + "<p><a href=\"" + link
				+ "\">Active my account</a></p>" + "<br>" + "<p>Ignore this email if you do remember your password, "
				+ "or you have not made the request.</p>";

		helper.setSubject(subject);

		helper.setText(content, true);

		mailSender.send(message);
	}

	public void updateActive(String email) {
			User user = userDao.findByEmailAddress(email);
			System.out.println("here");
			user.setEnabled(true); 
			userDao.save(user);
}
}
