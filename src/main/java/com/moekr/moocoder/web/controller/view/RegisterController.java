package com.moekr.moocoder.web.controller.view;

import com.moekr.moocoder.logic.service.UserService;
import com.moekr.moocoder.util.exceptions.ServiceException;
import com.moekr.moocoder.web.dto.form.RegisterForm;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class RegisterController extends AbstractViewController {
	private final UserService userService;

	public RegisterController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/register.html")
	public String register() {
		if (isLogin()) {
			return "redirect:/";
		} else {
			return "register";
		}
	}

	@PostMapping("/register.html")
	public String register(@ModelAttribute @Valid RegisterForm form, Errors errors,
						   RedirectAttributes attributes) {
		if (isLogin()) {
			return "redirect:/";
		}
		try {
			checkErrors(errors);
			userService.register(form);
			attributes.addFlashAttribute("from", "register");
			attributes.addFlashAttribute("username", form.getUsername());
			return "redirect:/login.html";
		} catch (ServiceException e) {
			attributes.addFlashAttribute("username", form.getUsername());
			attributes.addFlashAttribute("email", form.getEmail());
			attributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/register.html";
		}
	}
}
