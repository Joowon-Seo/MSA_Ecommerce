package com.example.userservice.controller;

import com.example.userservice.vo.Greeting;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

	private final Environment env;
	private final Greeting greeting;

	@GetMapping("/health-check")
	public String status() {
		return "It's Working in user Service.";
	}

	@GetMapping("/welcome")
	public String welcome() {
//		return env.getProperty("greeting.message");
		return greeting.getMessage();
	}
}
