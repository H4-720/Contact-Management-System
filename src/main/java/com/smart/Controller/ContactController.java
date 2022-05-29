package com.smart.Controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entity.User;
import com.smart.helper.Message;

@Controller
public class ContactController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/")
	public String home() {
		
		return "home";
	}
	
	@RequestMapping("/about")
	public String About() {
		
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("user",new User());
		return "signup";
	}
	
	@PostMapping("/register")
	public String do_signup(@Valid  @ModelAttribute("user") User user, BindingResult result1, @RequestParam(value = "agreement",defaultValue = "false") boolean agreement, Model model,HttpSession session ) {
		
		System.out.println("User"+user);
		try {
			
			if(!agreement) {
				throw new Exception("You have not agreed the terms and condition");
			}
			if(result1.hasErrors()) {
				model.addAttribute("user",user);
				return "signup";
			}
			user.setRole("USER");
			user.setActive(true);
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setAbout("HelloWorld");
			User result = this.userRepository.save(user);
			model.addAttribute("User",result);
			session.setAttribute("message", new Message("Successfully Register!!", "alert-success"));
			return "signup";
		}
		catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Someting went wrong!!" + e.getMessage(), "alert-danger"));
		}
		
		return "signup";
	}
	
	@GetMapping("/signin")
	public String customLogin(Model m) {
		m.addAttribute("title","Login-page");
		return "login";   
	}

}
