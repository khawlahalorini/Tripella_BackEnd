package com.codeninja.tripella.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import com.codeninja.tripella.dao.ReviewDao;
import com.codeninja.tripella.service.GetBean;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
	private String photo;
	
	@JsonIgnore
	@OneToMany(mappedBy="post")
	private List<Review> reviews;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "wishlist")
	private List<User> wishlistedBy;
	
	@JsonIgnore
	@OneToMany(mappedBy="post")
	private List<Detail> detail;
	
	@Column(name = "createdAt", nullable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(name = "updatedAt", nullable = false, updatable = true)
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
	@Transient
	@JsonIgnore
	private List<MultipartFile> photoFiles;

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
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public List<Review> getReviews() {
		return reviews;
	}
	
	@Transient
	private List<Review> reviewsList;
	
	@JsonGetter
	public List<Review> reviews(){
		return reviewsList;
	}
	
	public void appendReviews() {
		reviewsList = new ArrayList<Review>();
		ReviewDao reviewDao = (ReviewDao) GetBean.name("reviewDao");
		reviewsList.addAll(reviewDao.findAllByPost_Id(this.id));
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
	
	@JsonGetter
	public double averageRating() {
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
	
	@JsonGetter
	public Long reviewsCount() {
		ReviewDao reviewDao = (ReviewDao) GetBean.name("reviewDao");
		return reviewDao.countByPost_Id(this.id);
	}
	
	@Transient
	@JsonIgnore
	private List<String> photosList;
	
	@JsonGetter
	public List<String> photos() {
		return photosList;
	}
	
	public void setPhotosList(List<String> photosList) {
		this.photosList = photosList;
	}
	
	@JsonIgnore
	public List<MultipartFile> getPhotoFiles() {
		return photoFiles;
	}

	public void setPhotoFiles(List<MultipartFile> photoFiles) {
		this.photoFiles = photoFiles;
	}
}
