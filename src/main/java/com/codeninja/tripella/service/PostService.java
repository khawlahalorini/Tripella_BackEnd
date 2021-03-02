package com.codeninja.tripella.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.codeninja.tripella.dao.PostDao;
import com.codeninja.tripella.model.Post;

@Service
public class PostService {
	@Autowired
	PostDao postDao;
	
	@Autowired
	PhotoService photoService;

	public Post addPost(Post post) {

		try {
			postDao.save(post);
			return post;
		} catch (Exception e) {
			return null;
		}
	}

	public Iterable<Post> getPosts() {
		return postDao.findAll();
	}

	public Post postDetails(int id) {
		
		if(postDao.existsById(id)) {
			Post post = postDao.findById(id);
			post.appendReviews();
			post.setPhotosList(photoService.getPhotosList("post/"+id));
			return post;
		}
		
		return null;
	}

	public Post updatePost(Post post) {
		try {
			return postDao.save(post);
		} catch (Exception e) {
			return null;
		}
	}

	public boolean deletePost(int id) {
		try {
			postDao.deleteById(id);
			deleteAllPhotos(id);
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public Integer uploadPhotos(int postID, MultipartFile[] photos) {
		try {
			if(!postDao.existsById(postID)) throw new EntityNotFoundException();
			return photoService.upload(photos, "post/"+postID);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public boolean deletePhoto(String Photo) {
		return photoService.delete(Photo);
	}
	
	//should be called only when a post is deleted
	public boolean deleteAllPhotos(int postId) {
		return photoService.delete("post/"+postId);
	}
}
