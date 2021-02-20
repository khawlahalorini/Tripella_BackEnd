package com.codeninja.tripella.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Detail {
	@Id
	@GeneratedValue
	private int id;
	private Date dateTime;
	
	
	@ManyToOne
    @JoinColumn(name="trip_id", nullable=false)
    private Trip trip;

//	@ManyToOne
//  @JoinColumn(name="post_id", nullable=false)
//  private Post post;

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
	
	
	
}
