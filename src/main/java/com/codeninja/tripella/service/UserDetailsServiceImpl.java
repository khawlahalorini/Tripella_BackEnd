package com.codeninja.tripella.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.codeninja.tripella.dao.UserDao;
import com.codeninja.tripella.model.User;
import com.codeninja.tripella.model.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserDao userDao;
	
	@Override
	public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
		
		User user = userDao.findByEmailAddress(emailAddress);
		UserDetailsImpl userDetailsObj = new UserDetailsImpl(user);
		
		return userDetailsObj;
	}
}