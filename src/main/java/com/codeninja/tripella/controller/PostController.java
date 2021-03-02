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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

<<<<<<< HEAD

=======
import com.codeninja.tripella.dao.PostDao;
>>>>>>> 5be86fb61b591094f8bd4358a54b107b96c51c7f
import com.codeninja.tripella.model.Post;
import com.codeninja.tripella.service.PostService;

@RestController
public class PostController {

	@Autowired
	PostDao postDao;
	
	@Autowired
	PostService postService;

	@PostMapping("/post/add")
	public ResponseEntity<?> addPost(@RequestBody Post post) {
		Post body = postService.addPost(post);
		return body == null
				? ResponseEntity.badRequest().body("Not added")
				: ResponseEntity.ok(body);
	}
	
	@PutMapping(value="/post/photos/{postId}", consumes="multipart/form-data")
	public ResponseEntity<?> uploadPhotos(@PathVariable int postId, @RequestPart MultipartFile[] photos) throws Exception{
		Integer result =  postService.uploadPhotos(postId,photos);
		return result == null
				? ResponseEntity.badRequest().body("Upload was interrupted") 
				: ResponseEntity.accepted().body(result +" photos out of "+photos.length+" was uploaded");
	}
	
	@DeleteMapping("/post/photo/")
	public ResponseEntity<?> deletePhoto(@RequestParam String photo){
		return postService.deletePhoto(photo)
				? ResponseEntity.ok("Deleted: "+photo)
				: ResponseEntity.badRequest().build();
	}
	
	//TODO: set post main photo

	@GetMapping("/post/all")
	public ResponseEntity<?> getPosts() {
		return ResponseEntity.ok(postService.getPosts());
	}

	@GetMapping("/post/detail")
	public ResponseEntity<?> postDetails(@RequestParam int id) {
		Post body = postService.postDetails(id);
		return body == null
				? ResponseEntity.badRequest().body("Post not found")
				: ResponseEntity.ok(body);
	}

	@PutMapping("/post/update")
	public ResponseEntity<?> updatePost(@RequestBody Post post) {
		Post body = postService.updatePost(post);
		return body == null
				? ResponseEntity.badRequest().body("Not updated")
				: ResponseEntity.accepted().body(body);
	}

	@DeleteMapping("/post/delete")
	public ResponseEntity<?> deletePost(@RequestParam int id) {
		return postService.deletePost(id)
				? ResponseEntity.accepted().body("DELETED!!")
				: ResponseEntity.badRequest().body("Error!!");
	}
}
