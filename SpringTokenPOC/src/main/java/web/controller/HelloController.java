package web.controller;

import java.util.ArrayList;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import config.TokenHandler;
import config.UserService;
 
@Controller
public class HelloController {
 
	@RequestMapping(value = { "/", "/welcome**" }, method = RequestMethod.GET)
	public ModelAndView welcomePage() {
 
		UserService us = new UserService();
        us.addUser(new User("admin", "admin", new ArrayList<GrantedAuthority>() {{ new SimpleGrantedAuthority("ROLE_ADMIN"); }} ));
		TokenHandler th = new TokenHandler("tooManySecrets", us);
		User admin = us.loadUserByUsername("admin");
		String token = th.createTokenForUser(admin);
		
		ModelAndView model = new ModelAndView();
		model.addObject("title", "Spring Security Hello World");
		model.addObject("message", "Try to access /admin with and without this token in the X-AUTH-TOKEN header field<br><br>" + token);
		model.setViewName("hello");
		return model;
 
	}

	@RequestMapping(value = "/admin**", method = RequestMethod.GET)
	public ModelAndView adminPage() {
 
		ModelAndView model = new ModelAndView();
		model.addObject("title", "Spring Security Hello World");
		model.addObject("message", "This is a protected page, the Admin Page!");
		model.setViewName("admin");
 
		return model;
 
	}
 
	@RequestMapping(value = "/dba**", method = RequestMethod.GET)
	public ModelAndView dbaPage() {
 
		ModelAndView model = new ModelAndView();
		model.addObject("title", "Spring Security Hello World");
		model.addObject("message", "This is a protected page, the Database Page!");
		model.setViewName("admin");
 
		return model;
 
	}
 
}
