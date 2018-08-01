package pl.jstk.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import pl.jstk.service.UserService;

@ControllerAdvice
public class CurrentUserController {
	@ModelAttribute("currentUser")
	public UserService getCurrentUser(@AuthenticationPrincipal UserService currentUser) {
		return (currentUser == null) ? null : currentUser;
	}
}
