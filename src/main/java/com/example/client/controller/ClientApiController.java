package com.example.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.client.dto.UserResponse;
import com.example.client.service.RestTemplateService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/client")
public class ClientApiController {
	
	@Autowired
	private RestTemplateService service;
	
	@GetMapping("")
	public UserResponse getHello() {
		
		return service.hello();
	}
	
	@PostMapping("")
	public void post() {
		log.info("post메서드 호출");
		service.post();
	}
}
