package com.codeninja.tripella.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import javax.mail.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codeninja.tripella.model.UserDetailsImpl;
import com.codeninja.tripella.service.UserService;

@RestController
public class UserController {

	@Autowired
	UserService userService; 
	
	@PostMapping("/user/register")
	public ResponseEntity<?> register(@RequestBody HashMap<String,String> userData) throws UnsupportedEncodingException, MessagingException {
		
		return userService.register(userData);
	}

	@PostMapping("/user/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody HashMap<String,String> userData) {
		
		return userService.authenticate(userData); 
	}
	
	@GetMapping("/user/detail")
	public ResponseEntity<?> getUserDetails(@RequestParam(required = false) Integer id, @AuthenticationPrincipal UserDetailsImpl currentUser) {

		return userService.getUserDetails(id,currentUser);
	}

	@PutMapping("/user/update")
	public ResponseEntity<?> updateUser(@RequestBody HashMap<String,String> userData,
			@AuthenticationPrincipal UserDetailsImpl currentUser) {
		System.out.println(userData.toString());
		return userService.updateUser(userData,currentUser);
	}
	
	@PutMapping(value="/user/photo", consumes="multipart/form-data")
	public ResponseEntity<?> updatePhoto(@RequestPart MultipartFile photoFile, @AuthenticationPrincipal UserDetailsImpl currentUser) throws Exception{
		String body = userService.updatePhoto(photoFile,currentUser);
		return body == null
				? ResponseEntity.badRequest().body("Upload failed")
				: ResponseEntity.ok(body);
	}
	
	@DeleteMapping("/user/delete")
	public ResponseEntity<?> deleteUser(@RequestParam int id, @AuthenticationPrincipal UserDetailsImpl currentUser) {
		
		return userService.deleteUser(id, currentUser);
	}
	
	
	@PutMapping("/user/forgotpassword")
	public void handleresetpassword(@RequestParam String email) throws UnsupportedEncodingException, MessagingException {
		userService.handleresetpassword(email);
	}
    
	
    @PostMapping("/user/forgotpassword/resetpassword/updatepassword") 
    public void updatePassword(@RequestParam String newPassword, @RequestParam String token) {
    	userService.updatePassword(newPassword, token);
    }
    
    @PutMapping("/user/changepassword")
    public ResponseEntity<?> updatePassword(@RequestBody HashMap<String,String> userData, @AuthenticationPrincipal UserDetailsImpl currentUser) {
    	Boolean result = userService.updatePassword(userData,currentUser);
    	
    	if(result == null)
    		return ResponseEntity.badRequest().body("Faild to update password");
    	
    	return result ? ResponseEntity.ok("Password is updated")
    				  : ResponseEntity.status(HttpStatus.FORBIDDEN).body("Incorrect password");
    }
    
    @PostMapping("/user/active")
    public ResponseEntity<?> updateActive(@RequestParam String email) {
    	return userService.updateActive(email);
    }

	@PutMapping("/user/updaterole")
	public ResponseEntity<?> updateUserRole(@RequestBody HashMap<String,String> userData,@AuthenticationPrincipal UserDetailsImpl currentUser){
		return userService.updateUserRole(currentUser,userData);
	}
}
