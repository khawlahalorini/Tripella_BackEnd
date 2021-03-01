package com.codeninja.tripella.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codeninja.tripella.model.Post;
import com.codeninja.tripella.service.PostService;

@RestController
public class PostController {
	
	@Autowired
	PostService postService; 

	@PostMapping("/post/add")
	public ResponseEntity<?> addPost(@RequestBody Post post) {

		return postService.addPost(post);
	}

	@GetMapping("/post/all")
	public Iterable<Post> getPosts() {
		return postService.getPosts();
	}

	@GetMapping("/post/detail/{id}")
	public ResponseEntity<?> postDetails(@PathVariable int id) {
		
		
		return postService.postDetails(id);
	}

	@PutMapping("/post/update")
	public ResponseEntity<?> updatePost(@RequestBody Post post) {
		return postService.updatePost(post);
	}

	@DeleteMapping("/post/delete")
	public ResponseEntity<?> deletePost(@RequestParam int id) {
			return postService.deletePost(id);
	}
}
