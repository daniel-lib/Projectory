package com.app.projectory.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.projectory.doa.UsersRepository;
import com.app.projectory.entity.Users;
import com.app.projectory.service.UserLoginService;

@Controller
@RequestMapping("")
public class HomeController {

	UserLoginService userLgServ = new UserLoginService();
	@Autowired
	UsersRepository userDao;

	@GetMapping("/api")
	public String display(@RequestParam String name) {
		return "hello," + name + "</br> where are you from?";
	}

	@GetMapping
	public String displayLandingPage(Model model, Users user) {
		model.addAttribute("user", user);
		model.addAttribute("userLevel", "guest");
		/*
		 * List<Users> listOfHardCodedUsers = userLgServ.populateUserTest(); List<Users>
		 * allUsers = userDao.findAll(); for(Users userToAdd : listOfHardCodedUsers) {
		 * 
		 * int existence = userLgServ.doesUserExist(userToAdd.getUsername(), allUsers);
		 * if(existence == -1) { userDao.save(userToAdd); }
		 * 
		 * // if(!userToAdd.getUsername().equals(user))
		 * 
		 * }
		 */

		return "/home/landingPage";
	}

	
	  @PostMapping("/login") 
	  public String validateUser(Model model, Users user, RedirectAttributes redAttr) { 
		  List<Users> allUsers = userDao.findAll();
		  Users checkedUser = userLgServ.authenticateUser(user.getUsername(), user.getPassword(), allUsers); 
		  String indicator = "err"; 
		  if(checkedUser != null) { //		  
			 
			  String indicatorString = UUID.randomUUID().toString();
			  checkedUser.setLoginIndicator(indicatorString);
			  model.addAttribute("user", checkedUser); //redAttr.addAttribute("userd", checkedUser);
			  redAttr.addFlashAttribute("username", checkedUser.getUsername()); //
			  redAttr.addAttribute("firstName", checkedUser.getFirstName()); //
			  redAttr.addAttribute("lastName", checkedUser.getFirstName());
			  redAttr.addFlashAttribute("userA", checkedUser); 
			  checkedUser.setLoginIndicator(indicatorString);
			  userDao.save(checkedUser);
			  redAttr.addFlashAttribute("loginIndicator", indicatorString);
			  indicator = "pass"; 
			  return "redirect:/user/dashboard?indicator="+indicator+"&user="+checkedUser.getUserId()+"&proof="+indicatorString; 
	  }
	  
	  return "redirect:/?indicator="+indicator; 
//	  return "redirect: /user/dashboard?indicator=\""+indicator+"\""; 
	  }
	 

}
