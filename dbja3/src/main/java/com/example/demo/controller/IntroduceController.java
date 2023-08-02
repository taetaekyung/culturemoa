package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IntroduceController {

	@GetMapping("/teaminfo")
	public void teaminfo() {
	}
	
	@GetMapping("/pageinfo")
	public void pageinfo() {
	}
	
}
