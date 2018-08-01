package pl.jstk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import pl.jstk.constants.ViewNames;

@Controller
public class LoginController {

	@GetMapping(value = "/login")
	public String login() {
		return "login";
	}

	@GetMapping(value = "/logout")
	public String logout(Model model) {
		return ViewNames.LOGIN;
	}

}
