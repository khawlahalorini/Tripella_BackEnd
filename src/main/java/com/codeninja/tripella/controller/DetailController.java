package com.codeninja.tripella.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codeninja.tripella.dao.DetailDao;
import com.codeninja.tripella.model.Detail;

@RestController
public class DetailController {

	@Autowired
	DetailDao dao;

	@PostMapping("/detail/add")
	public Detail addDetail(@RequestBody Detail detail) {
		dao.save(detail);

		return detail;
	}

	@GetMapping("/detail/index")
	public Iterable<Detail> getDetails() {
		var details = dao.findAll();
		return details;
	}

	@GetMapping("/detail/detail")
	public Detail detailDetails(@RequestParam int id) {

		Detail detail = dao.findById(id);

		return detail;

	}

	@PutMapping("/detail/edit")
	public Detail editDetail(@RequestBody Detail detail) {
		dao.save(detail);

		return detail;
	}

	@DeleteMapping("/detail/delete")
	public boolean deleteDetail(@RequestParam int id) {

		dao.deleteById(id);
		return true;
	}

}
