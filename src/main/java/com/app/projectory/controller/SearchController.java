package com.app.projectory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.projectory.dao.SearchRepository;
import com.app.projectory.dto.SearchResultDto;

@Controller
@RequestMapping("/search")
public class SearchController {
	
	@Autowired
	SearchRepository searchRepo;
	
	@GetMapping("/users/{keyword}")
	public @ResponseBody String searchUsers() {
		return null;
	}
	
	@GetMapping("/projects/{keyword}")
	public @ResponseBody String searchProjects() {
		return null;
	}
	
	@GetMapping("/all/{keyword}")
	public @ResponseBody List<SearchResultDto> searchEverything(@PathVariable String keyword) {
		return searchRepo.searchEverything(keyword);
	}

}
