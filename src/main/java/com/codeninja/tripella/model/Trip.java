package com.codeninja.tripella.model;

import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.FutureOrPresent;

@Entity
public class Trip {
	@Id
	@GeneratedValue
	private int id;
	
	private String name;
	
	@FutureOrPresent 
	private Date start;
	
	private Date end;
	
	@ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
	
	@ManyToMany
	@JoinTable(name = "user_share", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "trip_id")})
	private List<User> sharedWith;
	
	@OneToMany(mappedBy = "trip")
	private List<Detail> detail;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Detail> getDetail() {
		return detail;
	}

	public void setDetail(List<Detail> detail) {
		this.detail = detail;
	}

	public List<User> getSharedWith() {
		return sharedWith;
	}

	public void setSharedWith(List<User> sharedWith) {
		this.sharedWith = sharedWith;
	}
	
}
