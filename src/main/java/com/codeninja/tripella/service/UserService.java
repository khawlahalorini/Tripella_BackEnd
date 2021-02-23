package com.codeninja.tripella.service;

import java.io.UnsupportedEncodingException;
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

	public ResponseEntity<?> register(HashMap<String, String> userData) {

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
		User user = userDao.findByEmailAddress(email);
		String confirmationToken = UUID.randomUUID().toString();
		user.setConfirmationToken(confirmationToken);
		String resetPasswordLink = "http://localhost:8080/tripella/user/handleresetpassword/resetpassword?token="
				+ confirmationToken;
		sendEmail(email, resetPasswordLink);
	}

	public String updatePassword(String newPassword, String token) {
		User user = userDao.findByConfirmationToken(token);
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

}
