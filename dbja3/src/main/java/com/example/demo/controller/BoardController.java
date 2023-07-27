package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.dao.BoardDAO_jpa;
import com.example.demo.entity.Board;

import lombok.Setter;

@Controller
@Setter
public class BoardController {
	
	@Autowired
	private BoardDAO_jpa boarddao_jpa;
	
	@GetMapping("/boards/board/freelist")
	public void freelist(Model model) {
		List<Board> list = boarddao_jpa.findByBcategory("자유");
		list = list.subList(0, 25);
		model.addAttribute("list", list);
	}
}
