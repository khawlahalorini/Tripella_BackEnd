package com.codeninja.tripella.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Detail {
	@Id
	@GeneratedValue
	private int id;
	private Date dateTime;
	
	
	@ManyToOne
    @JoinColumn(name="trip_id")
    private Trip trip;

	@ManyToOne
    @JoinColumn(name="post_id")
    private Post post;
	
	
	@Transient
	private int post_id; 
	
	@Transient
	private int trip_id; 

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}

	public Post getPost() {
		return post;
	}

	@JsonGetter
	public void setPost(Post post) {
		this.post = post;
	}

	@JsonIgnore
	public int getPost_id() {
		return post_id;
	}

	public void setPost_id(int post_id) {
		this.post_id = post_id;
	}

	@JsonIgnore
	public int getTrip_id() {
		return trip_id;
	}
	
	@JsonGetter
	public void setTrip_id(int trip_id) {
		this.trip_id = trip_id;
	}


	
	
}
