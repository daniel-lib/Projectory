package com.app.projectory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.projectory.dao.UsersRepository;
import com.app.projectory.entity.Users;
import com.app.projectory.service.UserAuthService;
import com.app.projectory.service.userAccountService;

@Controller
@RequestMapping("")
public class UserAuthController {
	
	UserAuthService userLgServ = new UserAuthService();
	
	@Autowired
	userAccountService accServ;
	@Autowired
	UsersRepository userDao;
	
	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	  @PostMapping("/register-user") 
	  @ResponseBody
	  public String registerUser(Model model, Users user, RedirectAttributes redAttr) { 
		  List<Users> allUsers = userDao.findAll();
		  Users checkedUser = userLgServ.authenticateUser(user.getUsername(), user.getPassword(), allUsers); 
		  String indicator = "err"; 
		  
		  if(checkedUser == null) { //		  
			  
			  user.setPassword(encoder.encode(user.getPassword()));
			  
			  userDao.save(user);
			  			 
				/*
				 * String indicatorString = UUID.randomUUID().toString();
				 * checkedUser.setLoginIndicator(indicatorString); model.addAttribute("user",
				 * checkedUser); //redAttr.addAttribute("userd", checkedUser);
				 * redAttr.addFlashAttribute("username", checkedUser.getUsername());
				 */
			  model.addAttribute("Registration-Successful");
			  return "redirect:/user/dashboard?reg=success";
	  }
		  
		  redAttr.addFlashAttribute("loginStatus", indicator);
	  return "redirect:/"; 
	  }
}
