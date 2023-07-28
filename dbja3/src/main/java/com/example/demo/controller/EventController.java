package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.demo.dao.EventDAO_jpa;
import com.example.demo.dao.EventDAO_mb;
import com.example.demo.entity.Event;

import lombok.Setter;

@Controller
@Setter
public class EventController {
	
	@Autowired
	private EventDAO_jpa eventdao_jpa;
	@Autowired
	private EventDAO_mb eventdao_mb;
	
	//행사 상세
	@GetMapping("/event/eventdetail")
	public String eventDetail(@RequestParam int eventno, Model model) {
	    // eventno를 사용하여 해당 이벤트를 조회합니다. (여기에서는 이벤트를 찾는 로직을 추가해야 합니다.)
	    Event event = eventdao_jpa.findByEventno(eventno);
	    model.addAttribute("event", event);
	    return "/event/eventdetail"; // "/event/eventdetail" 페이지로 이동하도록 반환합니다.
	}

	
	//연극 행사리스트
	@GetMapping("/event/playlist")
	public void playlist(Model model, @RequestParam(defaultValue = "1") int page) {
	    int pageSize = 16; // 한 페이지에 보여줄 개수 (4열 4행이므로 16개씩)
	    Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by("eventno").descending());
	    Page<Event> eventPage = eventdao_jpa.findByCategoryno(4, pageable); // categoryno가 2인 데이터만 가져오도록 변경
	    List<List<Event>> rows = new ArrayList<>();
	    List<Event> currentRow = null;

	    for (Event event : eventPage.getContent()) {
	        if (currentRow == null || currentRow.size() >= 4) {
	            currentRow = new ArrayList<>();
	            rows.add(currentRow);
	        }
	        currentRow.add(event);
	    }

	    model.addAttribute("rows", rows);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", eventPage.getTotalPages());
	}
	
	//뮤지컬 행사리스트
	@GetMapping("/event/musicallist")
	public void musicallist(Model model, @RequestParam(defaultValue = "1") int page) {
	    int pageSize = 16; // 한 페이지에 보여줄 개수 (4열 4행이므로 16개씩)
	    Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by("eventno").descending());
	    Page<Event> eventPage = eventdao_jpa.findByCategoryno(3, pageable); // categoryno가 2인 데이터만 가져오도록 변경
	    List<List<Event>> rows = new ArrayList<>();
	    List<Event> currentRow = null;

	    for (Event event : eventPage.getContent()) {
	        if (currentRow == null || currentRow.size() >= 4) {
	            currentRow = new ArrayList<>();
	            rows.add(currentRow);
	        }
	        currentRow.add(event);
	    }

	    model.addAttribute("rows", rows);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", eventPage.getTotalPages());
	}
	
	//페스티벌 행사리스트
	@GetMapping("/event/festivallist")
	public void festivallist(Model model, @RequestParam(defaultValue = "1") int page) {
	    int pageSize = 16; // 한 페이지에 보여줄 개수 (4열 4행이므로 16개씩)
	    Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by("eventno").descending());
	    Page<Event> eventPage = eventdao_jpa.findByCategoryno(5, pageable); // categoryno가 2인 데이터만 가져오도록 변경
	    List<List<Event>> rows = new ArrayList<>();
	    List<Event> currentRow = null;

	    for (Event event : eventPage.getContent()) {
	        if (currentRow == null || currentRow.size() >= 4) {
	            currentRow = new ArrayList<>();
	            rows.add(currentRow);
	        }
	        currentRow.add(event);
	    }

	    model.addAttribute("rows", rows);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", eventPage.getTotalPages());
	}
	
	//국내공연 행사리스트
	@GetMapping("/event/domesticconcertlist")
	public void domesticconcertlist(Model model, @RequestParam(defaultValue = "1") int page) {
	    int pageSize = 16; // 한 페이지에 보여줄 개수 (4열 4행이므로 16개씩)
	    Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by("eventno").descending());
	    Page<Event> eventPage = eventdao_jpa.findByCategoryno(1, pageable);
	    List<List<Event>> rows = new ArrayList<>();
	    List<Event> currentRow = null;

	    for (Event event : eventPage.getContent()) {
	        if (currentRow == null || currentRow.size() >= 4) {
	            currentRow = new ArrayList<>();
	            rows.add(currentRow);
	        }
	        currentRow.add(event);
	    }

	    model.addAttribute("rows", rows);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", eventPage.getTotalPages());
	}


	
	//내한공연 행사리스트
	@GetMapping("/event/koreaconcertlist")
	public void koreaconcertlist(Model model, @RequestParam(defaultValue = "1") int page) {
	    int pageSize = 16; // 한 페이지에 보여줄 개수 (4열 4행이므로 16개씩)
	    Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by("eventno").descending());
	    Page<Event> eventPage = eventdao_jpa.findByCategoryno(2, pageable); // categoryno가 2인 데이터만 가져오도록 변경
	    List<List<Event>> rows = new ArrayList<>();
	    List<Event> currentRow = null;

	    for (Event event : eventPage.getContent()) {
	        if (currentRow == null || currentRow.size() >= 4) {
	            currentRow = new ArrayList<>();
	            rows.add(currentRow);
	        }
	        currentRow.add(event);
	    }

	    model.addAttribute("rows", rows);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", eventPage.getTotalPages());
	}
	
	//행사 상세로 이동
//	@GetMapping("/event/eventdetail")
//	public void eventDetail() {
//	}
}
