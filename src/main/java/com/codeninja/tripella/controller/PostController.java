package com.codeninja.tripella.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codeninja.tripella.dao.PostDao;
import com.codeninja.tripella.model.Post;

@RestController
public class PostController {

	@Autowired
	PostDao postDao;

	@PostMapping("/post/add")
	public ResponseEntity<?> addPost(@RequestBody Post post) {

		try {
			postDao.save(post);
			return ResponseEntity.ok(post);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/post/all")
	public Iterable<Post> getPosts() {
		return postDao.findAll();
	}

	@GetMapping("/post/detail")
	public ResponseEntity<?> postDetails(@RequestParam int id) {
		Post post = postDao.findById(id);
		return (post != null)? ResponseEntity.ok(post)
							 : ResponseEntity.badRequest().body("Post not found");
	}

	@PutMapping("/post/edit")
	public ResponseEntity<?> editPost(@RequestBody Post post) {
		try {
			postDao.save(post);
			return ResponseEntity.accepted().body(post);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/post/delete")
	public ResponseEntity<?> deletePost(@RequestParam int id) {
		try {
			Post post = postDao.findById(id);
			postDao.deleteById(id);
			return ResponseEntity.accepted().body(post.getTitle() + " is DELETED!!");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Does not exist!!");
		}
	}

}
