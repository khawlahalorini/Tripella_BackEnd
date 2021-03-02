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
import org.springframework.web.multipart.MultipartFile;

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
	
	@Autowired
	PhotoService photoService;
	
	public ResponseEntity<?> register(HashMap<String, String> userData)throws UnsupportedEncodingException, MessagingException {

		User user = new User(userData);

		if (userDao.existsByEmailAddress(user.getEmailAddress())) {
			return ResponseEntity.badRequest().body("Email already exists");
		}

		user.setPassword(encryptPassword(user.getPassword()));

		userDao.save(user);
		// TODO: get link from application.properties using @Value("${.........}")
		String activeLink = "http://localhost:8080/tripella/user/active?email="
				+ user.getEmailAddress();
		//sendEmailForActive(user.getEmailAddress(),activeLink );
		return ResponseEntity.ok("registered, check your email to activate your account");
	}

	private String encryptPassword(String password) {
		return new BCryptPasswordEncoder().encode(password);
	}
	
	public ResponseEntity<?> authenticate(HashMap<String, String> userData) {

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(userData.get("emailAddress"), userData.get("password")));
		} catch (BadCredentialsException e) {
			return ResponseEntity.badRequest().body("Incorrect username or password");
		}

		UserDetails userDetails = userDetailsService.loadUserByUsername(userData.get("emailAddress"));
		HashMap<String,String> token = new HashMap<String,String>(); token.put("token",jwtUtil.generateToken(userDetails));
		return ResponseEntity.ok(token);
	}
	
	public ResponseEntity<?> getUserDetails(Integer id, UserDetailsImpl currentUser) {
		User user = null;
		user = (id !=null && userDao.existsById(id) && currentUser.isAdmin())
					? userDao.findById(id.intValue())
					: userDao.findById(currentUser.getId());
		
		return ResponseEntity.ok(user);
	}

	public ResponseEntity<?> updateUser(HashMap<String, String> userData, UserDetailsImpl currentUser) {
		User user = userDao.findById(currentUser.getId());
		user.update(userData);

		try {
			userDao.save(user);
			return ResponseEntity.accepted().body("Updated");
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.badRequest().body("Not Updated");
		}
	}

	public String updatePhoto(MultipartFile photoFile, UserDetailsImpl currentUser) throws Exception {
		User user = userDao.findById(currentUser.getId());

		if (/* photoFile != null && */ !photoFile.isEmpty()) {

			try {
				String photo = photoService.upload(photoFile, "user");

				if (photo != null && !photo.isBlank()) {
					String oldPhoto = user.getPhoto();
					user.setPhoto(photo);
					userDao.save(user);
					photoService.delete(oldPhoto); // delete old photo
					return user.getPhoto();
				}
			} catch (Exception e) {
				return null;
			}
		}

		return null;

	}
	
	public ResponseEntity<?> updateUserRole(UserDetailsImpl currentUser, HashMap<String, String> userData) {
		
		if(!userData.containsKey("id") || !userData.containsKey("role"))
			return ResponseEntity.badRequest().body("requires id, role");
		
		int id;
		
		try {
			id = Integer.parseInt(userData.get("id"));
			if(!userDao.existsById(id) || id == currentUser.getId())
				throw new Exception();
		}catch(Exception e) {
			return ResponseEntity.badRequest().body("invalid id");
			}
		
		String newRole = userData.get("role");
		
		if(newRole.equalsIgnoreCase("admin") || newRole.equalsIgnoreCase("user") ) {
			userDao.findById(id).setUserRole("ROLE_"+newRole.toUpperCase());
			return ResponseEntity.accepted().body("Updated");
		}
		
		return ResponseEntity.badRequest().body("invalid");
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
		String resetPasswordLink = "http://localhost:8080/tripella/user/handleresetpassword/resetpassword?token=" // TODO: get link from application.properties using @Value("${.........}")
				+ confirmationToken; // change this in deployment
		sendEmail(email, resetPasswordLink);
	}

	public String updatePassword(String newPassword, String token) {
		User user = userDao.findByConfirmationToken(token);
		
		if(user != null) {
			user.setPassword(encryptPassword(newPassword));
			user.setConfirmationToken(null);
			userDao.save(user);
			return "succ";
		}
		return "Failed";
	}
	
	public Boolean updatePassword(HashMap<String,String> userData, UserDetailsImpl currentUser) {
		String oldPassword = userData.get("oldPassword");
    	String newPassword = userData.get("newPassword");
    	User user = userDao.findById(currentUser.getId());
    	
    	try {
    		if(!new BCryptPasswordEncoder().matches(oldPassword, user.getPassword())) {
        		return false; 
        	}
    		
			user.setPassword(encryptPassword(newPassword));
			userDao.save(user);
	    	return true;
	    	
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
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

		String subject = "Here's the link to activate your account";

		String content = "<p>Hello,</p>"
				+ "<p>Click the link below to acctivate your account:</p>" + "<p><a href=\"" + link
				+ "\">Activate my account</a></p>" + "<br>" + "<p>Ignore this email if "
				+ "you have not made the request.</p>";

		helper.setSubject(subject);

		helper.setText(content, true);

		mailSender.send(message);
	}

	public ResponseEntity<?> updateActive(String email) {
			User user = userDao.findByEmailAddress(email);
			user.setEnabled(true); 
			userDao.save(user);
			return ResponseEntity.ok("user is activated");
			
}
}
