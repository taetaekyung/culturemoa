package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dao.FAQDAO_jpa;
import com.example.demo.entity.FAQ;

@Controller
public class FAQController {

	@Autowired
	public FAQDAO_jpa faqdao_jpa;
	
	@GetMapping(value={"/FAQ","/FAQ/{keyword}"})
	public ModelAndView faqlist(Model model, @PathVariable(required=false) String keyword) {
		ModelAndView mav = new ModelAndView("/FAQ");
		if(keyword == null || keyword.equals("")) {
			List<FAQ> list = faqdao_jpa.findAll();
			mav.addObject("list", list);
		}
		else {
			List<FAQ> list = faqdao_jpa.findByKeyword(keyword);
			mav.addObject("list", list);
		}
		return mav;
	}
}
