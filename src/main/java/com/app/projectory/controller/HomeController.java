package com.app.projectory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.projectory.dao.UsersRepository;
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

	@GetMapping
	public String displayLandingPage(Model model, Users user, Authentication auth) {
		model.addAttribute("user", user);
		model.addAttribute("userLevel", "guest");
		/*
		 * if(auth.isAuthenticated()) { return "redirect:/user/dashboard"; }
		 */
		return "/home/landingPage";
	}

}
