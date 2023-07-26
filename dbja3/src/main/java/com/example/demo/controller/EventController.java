package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.dao.EventDAO_jpa;
import com.example.demo.dao.EventDAO_mb;

import lombok.Setter;

@Controller
@Setter
public class EventController {
	
	@Autowired
	private EventDAO_jpa dao_jpa;
	@Autowired
	private EventDAO_mb dao_mb;
	
	@GetMapping("/event/eventlist")
	public void eventList() {
	}
	   
	@GetMapping("/event/eventdetail")
	public void eventDetail() {
	}
}
