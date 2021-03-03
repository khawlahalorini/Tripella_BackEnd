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

import com.codeninja.tripella.dao.DetailDao;
import com.codeninja.tripella.model.Detail;
import com.codeninja.tripella.service.DetailService;

@RestController
public class DetailController {

	@Autowired
	DetailDao dao;

	@Autowired
	DetailService detailService; 
	
	@PostMapping("/detail/add")
	public ResponseEntity<?> addDetail(@RequestBody Detail detail) {

		return detailService.addDetail(detail);
	}

	@GetMapping("/detail/index")
	public Iterable<Detail> getDetails() {
		var details = dao.findAll();
		return details;
	}

	@GetMapping("/detail/detail")
	public ResponseEntity<?> detailDetails(@RequestParam int id) {

		return detailService.detailDetails(id);

	}

	@PutMapping("/detail/edit")
	public ResponseEntity<?> editDetail(@RequestBody Detail detail) {

		return detailService.editDetail(detail);
	}

	@DeleteMapping("/detail/delete")
	public boolean deleteDetail(@RequestParam int id) {
try {
	dao.deleteById(id);
	return true;
}
catch (Exception e) {
	return false; 
}
	}

}
