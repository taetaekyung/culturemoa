package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dao.BoardDAO_jpa;
import com.example.demo.entity.Board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;

@Controller
@Setter
public class BoardController {
	
	public int pageSIZE = 25;
	public int totalRecord;
	public int totalPage;
	
	@Autowired
	private BoardDAO_jpa boarddao_jpa;
	
	@GetMapping(value={"/boards/board/freelist", "/boards/board/freelist/", "/boards/board/freelist/{pageNUM}", 
			"/boards/board/freelist/{keyword}/{pageNUM}", "/boards/board/freelist/{keyword}/{pageNUM}/{orderby}"})
	public ModelAndView freelist(HttpSession session, @PathVariable(required = false) Integer pageNUM, @PathVariable(required=false) String keyword, 
			@PathVariable(required=false) String orderby, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/boards/board/freelist");

		System.out.println("원래 orderby "+orderby);
		if(orderby == null) {
			if(session.getAttribute("orderby") != null) {
				System.out.println("session의 orderby "+orderby);
				orderby = (String) session.getAttribute(orderby);
			}
			else {
				orderby="regdate";
			}	
		}
		
		String key = "all";
		if(pageNUM == null) {
			pageNUM = 1;
		}
		if(keyword == null) {
			key = "all";
		}
		totalRecord  = boarddao_jpa.getTotalRecord("자유");
		totalPage = (int)Math.ceil(totalRecord/(double)pageSIZE);
		int startRecord = (pageNUM-1)*pageSIZE+1;
		int endRecord = startRecord+pageSIZE-1;
		List<Board> list;
		
		if(session.getAttribute("keyword")!=null) {
			key = (String)session.getAttribute("keyword");
		}
		if(keyword != null) {
			key = keyword;
		}

		if(key.equals("all")) {
			if(orderby.equals("regdate")) {
				list = boarddao_jpa.findByBcategoryOrderByRegdate("자유", "", startRecord, endRecord);
			}
			else {
				list = boarddao_jpa.findByBcategoryOrderByBoardhit("자유", "", startRecord, endRecord);
			}
		}
		else {
			if(orderby.equals("regdate")) {
				list = boarddao_jpa.findByBcategoryOrderByRegdate("자유", keyword, startRecord, endRecord);
			}
			else {
				list = boarddao_jpa.findByBcategoryOrderByBoardhit("자유", keyword, startRecord, endRecord);
			}
		}
		session.setAttribute("keyword", key);
		session.setAttribute("orderby", orderby);

		
		mav.addObject("list", list);
		int startPage = (pageNUM/10)*10+1;
		int endPage = (pageNUM/10+1)*10;
		mav.addObject("startPage", startPage);
		mav.addObject("endPage", endPage);
		return mav;
	}
	
}
