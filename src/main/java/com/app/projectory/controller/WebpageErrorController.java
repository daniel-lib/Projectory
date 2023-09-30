package com.app.projectory.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebpageErrorController implements ErrorController{
	
	@GetMapping("/error")
	public String handleWebpageError(HttpServletRequest request) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		Integer statusCode = 0;
		if(status != null) {
			statusCode = Integer.valueOf(status.toString());
		}
		/*
		 * if(statusCode == HttpStatus.NOT_FOUND.value()) { return
		 * "pageError/error-404"; }
		 */
		//return "pageError/error-default";
		return "redirect:/";
	}
	
	/*
	 * @Override public String getErrorPath() { return "/error"; }
	 */
	
}
