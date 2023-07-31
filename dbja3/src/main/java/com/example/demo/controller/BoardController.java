package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dao.BoardDAO_jpa;
import com.example.demo.entity.Board;
import com.example.demo.entity.Event;

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
	
	@GetMapping(value={"/boards/board/freelist", "/boards/board/freelist/", "/boards/board/freelist/{page}", 
			"/boards/board/freelist/{keyword}/{page}", "/boards/board/freelist/{keyword}/{page}/{orderby}"})
	public ModelAndView freelist(HttpSession session, @PathVariable(required=false) String keyword, 
			@PathVariable(required=false) String orderby, HttpServletRequest request, @PathVariable(required = false) Integer page) {
		ModelAndView mav = new ModelAndView("/boards/board/freelist");

		if(orderby == null) {
			if(session.getAttribute("orderby") != null && !session.getAttribute("orderby").equals("")) {
				orderby = (String) session.getAttribute("orderby");
			}
			else {
				orderby="regdate";
			}	
		}
		String key = "all";
		if(page == null) {
			page = 1;
		}
		if(keyword == null) {
			key = "all";
		}

		Page<Board> list;
		
		if(session.getAttribute("keyword")!=null) {
			key = (String)session.getAttribute("keyword");
		}
		if(keyword != null) {
			key = keyword;
		}
		
		Pageable pageable;
		
		if(key.equals("all")) {
			if(orderby.equals("regdate")) {
				pageable = PageRequest.of(page-1, pageSIZE, Sort.by("regdate").descending());
			}
			else {
			    pageable = PageRequest.of(page-1, pageSIZE, Sort.by("boardhit").descending());
			}
			list = boarddao_jpa.findByBcategory("자유", "", pageable);
		}
		else {
			if(orderby.equals("regdate")) {
			    pageable = PageRequest.of(page-1, pageSIZE, Sort.by("regdate").descending());
			}
			else {
			    pageable = PageRequest.of(page-1, pageSIZE, Sort.by("boardhit").descending());
			}
			list = boarddao_jpa.findByBcategory("자유", keyword, pageable);

		}
		
	    List<List<Board>> rows = new ArrayList<>();
	    List<Board> boardlist = new ArrayList<Board>();
	    List<Board> currentRow = null;
		session.setAttribute("keyword", key);
		session.setAttribute("orderby", orderby);
		
	    for (Board board : list.getContent()) {
	    	
	    	boardlist.add(board);
	        if (currentRow == null || currentRow.size() >= 4) {
	            currentRow = new ArrayList<>();
	            rows.add(currentRow);
	        }
	        currentRow.add(board);
	    }
		
		mav.addObject("list", boardlist);
		mav.addObject("currentPage", page);
		mav.addObject("totalPages", list.getTotalPages());

		return mav;
	}
	
}
