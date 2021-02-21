package com.codeninja.tripella.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Post {

	@Id
	@GeneratedValue
	private int id;
	
	private String title;
	private String type;
	private String information;
	private String description;
	private String city;
	private String country;
	private double lat;
	private double lng;
	private String Photo;
	
	@OneToMany(mappedBy="post")
	private List<Review> reviews;
	
	@ManyToMany(mappedBy = "wishlist")
	private List<User> wishlistedBy;
	
	@Column(name = "createdAt", nullable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(name = "updatedAt", nullable = false, updatable = true)
	@UpdateTimestamp
	private LocalDateTime updatedAt;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getPhoto() {
		return Photo;
	}

	public void setPhoto(String photo) {
		Photo = photo;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public List<User> getWishlistedBy() {
		return wishlistedBy;
	}

	public void setWishlistedBy(List<User> wishlistedBy) {
		this.wishlistedBy = wishlistedBy;
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
	
	public double getAverageRating() {
		//reviews have to be > 0
		if (reviews.size() > 0) {
			int sumOfRatings = 0;
			for (Review r : reviews) {
				sumOfRatings += r.getRating();
			}
			return (sumOfRatings * 1.0) / reviews.size();
			
		} else
			return 0;
	}
}
