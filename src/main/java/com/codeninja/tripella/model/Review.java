package com.codeninja.tripella.model;

import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Review {
	
	@Id
	@GeneratedValue
	private int id;
	
	@NotNull
	private String content;
	
	@NotNull
	private int rating;
	
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
	
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name = "post_id")
	private Post post;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		if(rating < 1) rating = 1;   		//deny 0 ratings
		else if(rating > 5) rating = 5;		//deny 5+ ratings
		this.rating = rating;
	}

	public User getUser() {
		return user;
	}
	
	@JsonGetter
	public HashMap<String, String> user() {
		User user = getUser();
		return user.shortDetail();
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}
	
}
