package pl.jstk.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping(value = "/403")
	public Model handleException(Principal user, Model model) {
		if (user != null) {
			model.addAttribute("error", user.getName() + ", you don't have permission to access this page!");
		} else {
			model.addAttribute("error", "Sorry, you can't remove this books");
		}
		return model;
	}
}
