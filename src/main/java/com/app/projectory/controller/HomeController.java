package com.app.projectory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.projectory.dao.BlogRepository;
import com.app.projectory.dao.UsersRepository;
import com.app.projectory.dto.BlogPeekViewListDto;
import com.app.projectory.entity.Users;
import com.app.projectory.service.UserAuthService;
import com.app.projectory.service.userAccountService;

@Controller
@RequestMapping("")
public class HomeController {

	UserAuthService userLgServ = new UserAuthService();

	@Autowired
	userAccountService accServ;
	@Autowired
	UsersRepository userDao;
	@Autowired
	BlogRepository blogRepo;

	@GetMapping
	public String displayLandingPage(Model model, Users user, Authentication auth) {
		model.addAttribute("user", user);
		model.addAttribute("userLevel", "guest");
		/*
		 * if(auth.isAuthenticated()) { return "redirect:/user/dashboard"; }
		 */
		
		if(auth !=null && auth.isAuthenticated())
			return "redirect:/user/dashboard";
		else {
			List<BlogPeekViewListDto> mostRatedBlogPeekViewList = blogRepo.findLatestBlogsPeekView(3);
			model.addAttribute("MostRatedBlogPeekViewList", mostRatedBlogPeekViewList);
		return "/home/landingPage";
		}
	}
	
	@GetMapping("/about")
	public String serverAboutPage(Model model, Users user) {
		model.addAttribute("user", user);
		return "/home/about";
	}
	@GetMapping("/features")
	public String serverFeaturesPage(Model model, Users user) {
		model.addAttribute("user", user);
		return "/home/features";
	}

}
