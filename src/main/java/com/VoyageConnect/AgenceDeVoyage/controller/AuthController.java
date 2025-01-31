package com.VoyageConnect.AgenceDeVoyage.controller;

import com.VoyageConnect.AgenceDeVoyage.entity.User;
import com.VoyageConnect.AgenceDeVoyage.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthController {

	private final UserService userService;

	public AuthController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/register")
	public String showRegisterForm() {
		return "register";
	}

	@PostMapping("/register")
	@ResponseBody
	public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
		try {
			userService.registerUser(
				request.getUsername(),
				request.getFullName(),
				request.getPassword(),
				"ROLE_CLIENT"
			);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/login")
	public String showLoginForm() {
		return "login";
	}

	@GetMapping("/user-details")
	public ResponseEntity<?> getUserDetails(Authentication authentication) {
		if (authentication != null && authentication.isAuthenticated()) {
			String username = authentication.getName();
			Optional<User> user = userService.findByUsername(username);
			if (user.isPresent()) {
				return ResponseEntity.ok(user.get());
			}
		}
		return ResponseEntity.badRequest().body("User not found");
	}

	// Classe interne pour la requête
	private static class RegisterRequest {
		private String username;
		private String fullName;
		private String password;

		// Getters et setters
		public String getUsername() { return username; }
		public void setUsername(String username) { this.username = username; }
		public String getFullName() { return fullName; }
		public void setFullName(String fullName) { this.fullName = fullName; }
		public String getPassword() { return password; }
		public void setPassword(String password) { this.password = password; }
	}
}
