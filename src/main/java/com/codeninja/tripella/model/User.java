package com.codeninja.tripella.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {
	
	@Id
	@GeneratedValue
	private int id;
	@NotNull
	private String firstName;
	@NotNull
	private String lastName;
	@NotNull
	private String emailAddress;
	
	@JsonIgnore
	private String password;
	
	@JsonIgnore
	private String userRole;
	
	@JsonIgnore
	private boolean isEnabled = true;
	
	private String photo;
	
	@JsonIgnore
	private String confirmationToken;
	
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "wishlists", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "post_id")})
	private List<Post> wishlist;

	//Constructor to map and validate user data to user object
	public User(HashMap<String, String> userData) {
		setEmailAddress(userData.get("emailAddress"));
		setPassword(userData.get("password"));
		setFirstName(userData.get("firstName"));
		setLastName(userData.get("LastName"));
		setPhoto(userData.get("photo"));
	}

	public User() {
		super();
	}
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<Trip> trip;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "sharedWith")
	private List<Trip> inshare;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<Review> reviews;

	@Column(name = "createdAt", nullable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(name = "updatedAt", nullable = false, updatable = true)
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
	@Transient
	@JsonIgnore
	private MultipartFile photoFile;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserRole() {
		if(userRole == null || userRole.isBlank())  //default
			return "ROLE_USER";
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	@JsonIgnore //does't start with get
	public boolean isEnabled() {
		
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getPhoto() {
		return photo;
	}

	@JsonIgnore
	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public List<Trip> getTrip() {
		return trip;
	}

	public void setTrip(List<Trip> trip) {
		this.trip = trip;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<Post> getWishlist() {
		return wishlist;
	}

	public void setWishlist(List<Post> wishlist) {
		this.wishlist = wishlist;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public List<Trip> getInshare() {
		return inshare;
	}

	public void setInshare(List<Trip> inshare) {
		this.inshare = inshare;
	}
	
	public String getConfirmationToken() {
		return confirmationToken;
	}

	public void setConfirmationToken(String confirmationToken) {
		this.confirmationToken = confirmationToken;
	}

	public MultipartFile getPhotoFile() {
		return photoFile;
	}

	public void setPhotoFile(MultipartFile photoFile) {
		this.photoFile = photoFile;
	}

	public HashMap<String, String> shortDetail() {
		HashMap<String, String> map = new HashMap<>();
		map.put("id",String.valueOf(id));
		map.put("firstName", firstName);
		map.put("lastName", lastName);
		map.put("photo", getPhoto());
		return map;
	}
	
	//map updatable details only
	public void update(User userData) { 	
			setFirstName(userData.getFirstName());
			setLastName(userData.getLastName());
	}
	
}
