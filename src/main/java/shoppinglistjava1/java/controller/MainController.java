package shoppinglistjava1.java.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import shoppinglistjava1.java.beans.User;
import shoppinglistjava1.java.repository.UserRepository;


@Controller
public class MainController {

	@Autowired
	private UserRepository userRepo;

	@GetMapping("")
	public String index(Model model) {
		return "index";
	}
	
	@GetMapping("/home")
	public String home(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User v = userRepo.findOneByEmail(email);
//		v.setPassword(new BCryptPasswordEncoder().encode(v.getPassword()));
		return "home";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}

	@PostMapping("/login")
	public String loginSubmit() {
		return "index";
	}
	
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute(new User());
		return "signup";
	}

	@PostMapping("/signup")
	public String signupSave(@ModelAttribute @Valid User user,
			BindingResult result, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("user", user);
			return "signup";
		} else {
			user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
			userRepo.save(user);
			return "redirect:/";
		}

	}
	
	@GetMapping("/error")
	public String error(){
	return "error";
	}
	
	
	
}
