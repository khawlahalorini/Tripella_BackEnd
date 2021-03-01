package com.codeninja.tripella.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.codeninja.tripella.dao.PostDao;
import com.codeninja.tripella.model.Post;

@Service
public class PostService {
	@Autowired
	PostDao postDao;
	
	@Autowired
	PhotoService photoService;
	
	
	public ResponseEntity<?> addPost(Post post) {

		try {
			postDao.save(post);
			return ResponseEntity.ok(post);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	public Iterable<Post> getPosts() {
		return postDao.findAll();
	}
	
	public ResponseEntity<?> postDetails(int id) {
		
		if(postDao.existsById(id)) {
			Post post = postDao.findById(id);
			post.appendReviews();
			post.setPhotosList(photoService.getPostPhotos(id));
			return ResponseEntity.ok(post);
		}
		
		return ResponseEntity.badRequest().body("Post not found");
	}
	
	public ResponseEntity<?> updatePost(Post post) {
		try {
			postDao.save(post);
			return ResponseEntity.accepted().body(post);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	public ResponseEntity<?> deletePost(int id) {
		try {
			Post post = postDao.findById(id);
			postDao.deleteById(id);
			return ResponseEntity.accepted().body(post.getTitle() + " is DELETED!!");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Does not exist!!");
		}
	}
}
