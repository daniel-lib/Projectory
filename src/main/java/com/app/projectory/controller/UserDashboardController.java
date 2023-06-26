package com.app.projectory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.projectory.doa.TodoListRepository;
import com.app.projectory.entity.Todo;
import com.app.projectory.entity.Users;
import com.app.projectory.service.UserLoginService;

@Controller
@RequestMapping("/user")
public class UserDashboardController {
	
	@Autowired
	TodoListRepository todoData;
	
	UserLoginService userLgServ = new UserLoginService();
	
	@GetMapping("/dashboard")
	public String displayUserDashboard(Model model) {
		
	
//		return "/user/user-dashboard?indicator = pass"; 
		List<Todo> fetchedItems = todoData.findAll();
		/* Users LoggedInUser = userLgServ.getByUsername(user.getUsername()); */
		
		model.addAttribute("itemCount", todoData.count());	
		model.addAttribute("items", fetchedItems);
		model.addAttribute("user", model.getAttribute("userA"));
		
		/* model.addAttribute("userL", LoggedInUser); */
		 return "/user/user-dashboard"; 
		
	}

}
