package com.hard.study.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CsrfController {
	
	@RequestMapping(value="/csrf", method=RequestMethod.GET)
	public String getCSRF(HttpServletRequest req) {
		
		CsrfToken csrf = (CsrfToken) req.getAttribute(CsrfToken.class.getName());
		
		return csrf.getToken();
		
	}
	
}
