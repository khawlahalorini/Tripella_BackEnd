package com.codeninja.tripella.model;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {

	private String username;
	private String password;
	private String name;
	private int id;
	private boolean isEnabled;
	private String userRole;
	private List<GrantedAuthority> authorities;
	
	//Constructor
	public UserDetailsImpl(User user){
		if(user==null)
			throw new UsernameNotFoundException("Not found");
		
		username = user.getEmailAddress();
		password = user.getPassword();
		name = user.getFirstName();
		id = user.getId();
		isEnabled = user.isEnabled();
		userRole = user.getUserRole();
		authorities = Arrays.stream(user.getUserRole().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}
	
	public String getRole() {
		return userRole;
	}
	
	public boolean isAdmin() {
		return getRole().equals("ROLE_ADMIN");
	}
	

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

}
