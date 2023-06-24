package com.app.projectory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.projectory.entity.Users;
import com.app.projectory.service.UserLoginService;

@Controller
@RequestMapping("")
public class HomeController {
	

	UserLoginService userLgServ = new UserLoginService();
	
	
	@GetMapping
	public String displayLandingPage(Model model, Users user) {
		model.addAttribute("user", user);
		model.addAttribute("userLevel", "guest");
		return "/home/landingPage";
	}
	@PostMapping("/login")
	public String validateUser(Model model, Users user) {
		Users checkedUser = userLgServ.authenticateUser(user.getUsername(), user.getPassword());
		String indicator = "err";
		if(checkedUser != null) {
			model.addAttribute("user", checkedUser);
			indicator = "pass";
			return "redirect:/user/dashboard?indicator="+indicator;	
		}
		
		 return "redirect:/?indicator="+indicator; 
		/* return "redirect: /user/dashboard?indicator=\""+indicator+"\""; */
	}
	
}

