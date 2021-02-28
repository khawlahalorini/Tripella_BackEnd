package com.codeninja.tripella.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codeninja.tripella.dao.PostDao;

@Service
public class PostService {
	@Autowired
	PostDao postDao;
}
