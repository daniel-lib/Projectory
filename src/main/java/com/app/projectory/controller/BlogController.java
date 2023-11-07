package com.app.projectory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.projectory.dao.BlogRepository;
import com.app.projectory.dto.BlogPeekViewListDto;
import com.app.projectory.entity.Users;

@Controller()
public class BlogController {
	
	@Autowired
	BlogRepository blogRepo;
	
	@GetMapping("/blog")
	public String serveBlogPage(Model model, Users user) {
		int numberOfMostViewedBlogs = 3;
		List<BlogPeekViewListDto> mostRatedBlogPeekViewList = blogRepo.findLatestBlogsPeekView(numberOfMostViewedBlogs);
		model.addAttribute("MostRatedBlogPeekViewList", mostRatedBlogPeekViewList);
//		List<BlogPeekViewListDto> allBlogPeekViewList = blogRepo.findAllBlogsPeekView();
//		model.addAttribute("AllBlogPeekViewList", allBlogPeekViewList);
		List<BlogPeekViewListDto> allExceptMostViewedBlogsPeekView = blogRepo.findAllExceptMostViewedBlogsPeekView(numberOfMostViewedBlogs);
		model.addAttribute("AllExceptMostViewedBlogsPeekView", allExceptMostViewedBlogsPeekView);
		
		model.addAttribute("user", user);
		return "home/blog";
	}
	
	@GetMapping("/blog2")
	public @ResponseBody List<BlogPeekViewListDto> serveBlogPage2(Model model) {
		List<BlogPeekViewListDto> blogPeekViewList = blogRepo.findLatestBlogsPeekView(3);
		
		return blogPeekViewList; 
	}
	@GetMapping("full-blog-content")
	public @ResponseBody String getFullBlogContent(@PathVariable("blog") long blogId) {
		
		return null;
	}
}
