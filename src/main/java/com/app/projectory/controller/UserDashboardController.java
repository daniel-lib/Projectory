package com.app.projectory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserDashboardController {
	
	@GetMapping("/dashboard")
	public String displayUserDashboard() {
//		return "/user/user-dashboard?indicator = pass"; 
		return "/user/user-dashboard"; 
	}

}
