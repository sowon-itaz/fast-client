package com.example.inspector.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.inspector.dto.UserResponse;
import com.example.inspector.service.RestTemplateService;

@RestController
@RequestMapping("/api/client")
public class ClientApiController {
	
	@Autowired
	private RestTemplateService service;
	
	@GetMapping("")
	public UserResponse getHello() {
		
		return service.hello();
	}
}
