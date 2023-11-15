package com.app.projectory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
	@Autowired
	BCryptPasswordEncoder encoder;
	
	  @PostMapping("/register-user") 
	  public String registerUser(Model model, Users user, RedirectAttributes redAttr) { 
		  List<Users> allUsers = userDao.findAll();
		  Users checkedUser = userLgServ.authenticateUser(user.getUsername(), user.getPassword(), allUsers); 
		  String indicator = "err"; 
		  
		  
		//  if(checkedUser == null) { //		  
		  if(userDao.findByUsername(user.getUsername()) == null) { //		   
			  user.setPassword(encoder.encode(user.getPassword()));
			  user.setEnabled(true);
			  user.setRole("USER"); 		  
			  userDao.save(user);
			  model.addAttribute("Registration-Successful");
			  indicator = "success";
//			  return "redirect:/user/dashboard?reg=success";			  
		  	}
		  
		  redAttr.addFlashAttribute("loginStatus", indicator);
	  //return "redirect:/"; 
		  return "redirect:/?reg="+indicator; 
	  }
}
