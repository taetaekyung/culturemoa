package com.example.demo.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dao.EventDAO_jpa;
import com.example.demo.dao.EventDAO_mb;
import com.example.demo.entity.Event;
import com.example.demo.vo.EventVO;

import lombok.Setter;

@Controller
@Setter
public class EventController {
	
	@Autowired
	private EventDAO_jpa eventdao_jpa;
	@Autowired
	private EventDAO_mb eventdao_mb;
	//오늘 날짜
	LocalDate currentDate = LocalDate.now();
	
	//event 엔티티 eventvo 로 이동. 공연 진행 상태 추가해서 쓰려고
	public EventVO changeEventtoevenvo(Event event) {
		EventVO eventVO = new EventVO();
		eventVO.setEventno(event.getEventno());
		eventVO.setCategoryno(event.getCategoryno());
		eventVO.setEventname(event.getEventname());
		eventVO.setEventaddr(event.getEventaddr());
		eventVO.setEventplace(event.getEventplace());
		eventVO.setEventcontent(event.getEventcontent());
		eventVO.setEventstart(event.getEventstart());
		eventVO.setEventend(event.getEventend());
		eventVO.setEventlat(event.getEventlat());
		eventVO.setEventlong(event.getEventlong());
		eventVO.setEventhit(event.getEventhit());
		eventVO.setEventlink(event.getEventlink());
		eventVO.setEventfname(event.getEventfname());
		eventVO.setEventticket(event.getEventticket());
		eventVO.setEventprice(event.getEventprice());
		eventVO.setParkplace(event.getParkplace());
		return eventVO;
	}
	
	//공연중 상태 확인
    public String calculateEventStatus(Event event) {
        LocalDate currentDate = LocalDate.now(); // 현재 날짜를 가져옵니다.

        if (event.getEventstart() != null && event.getEventend() != null) {
            if (currentDate.isBefore((LocalDate)(event.getEventstart().toInstant()
            	      .atZone(ZoneId.systemDefault())
            	      .toLocalDate()))) {
                // 이벤트 시작일과 현재 날짜를 비교하여 D-day를 계산합니다.
                long daysUntilEvent = ChronoUnit.DAYS.between(currentDate, event.getEventstart().toInstant()
                	      .atZone(ZoneId.systemDefault())
                	      .toLocalDate());
                return ("D-" + daysUntilEvent);
            } else if (currentDate.isAfter(event.getEventend().toInstant().atZone(ZoneId.systemDefault())
            	      .toLocalDate())) {
            	return "공연종료";
            } else {
            	return "공연중";
            }
        } else {
            return "미정";
        }
    }
	
	//행사 상세
	@GetMapping("/event/eventdetail")
	public String eventDetail(@RequestParam int eventno, Model model) {
	    // eventno를 사용하여 해당 이벤트를 조회합니다. (여기에서는 이벤트를 찾는 로직을 추가해야 합니다.)
	    Event event = eventdao_jpa.findByEventno(eventno);
	    String state =  calculateEventStatus(event);
	    System.out.println(state);
	    System.out.println("오늘 날짜: " + currentDate);
	    
	    model.addAttribute("event", event);
	    model.addAttribute("state",state);
	    return "/event/eventdetail"; // "/event/eventdetail" 페이지로 이동하도록 반환합니다.
	}
	
	//국내공연 행사리스트
	@GetMapping("/event/domesticconcertlist")
	public void domesticconcertlist(Model model, @RequestParam(defaultValue = "1") int page) {
		int pageSize = 16; // 한 페이지에 보여줄 개수 (4열 4행이므로 16개씩)
	    Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by("eventno").descending());
	    Page<Event> eventPage = eventdao_jpa.findByCategoryno(1, pageable); // categoryno가 2인 데이터만 가져오도록 변경
	    List<List<EventVO>> rows = new ArrayList<>();
	    List<EventVO> currentRow = null;

	    for (Event event : eventPage.getContent()) {
		    String state =  calculateEventStatus(event);
		    EventVO eventVO = changeEventtoevenvo(event);
		    eventVO.setEventState(state);
		    
	        if (currentRow == null || currentRow.size() >= 4) {
	            currentRow = new ArrayList<>();
	            rows.add(currentRow);
	        }
	        currentRow.add(eventVO);
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
	    List<List<EventVO>> rows = new ArrayList<>();
	    List<EventVO> currentRow = null;

	    for (Event event : eventPage.getContent()) {
		    String state =  calculateEventStatus(event);
		    EventVO eventVO = changeEventtoevenvo(event);
		    eventVO.setEventState(state);
		    
	        if (currentRow == null || currentRow.size() >= 4) {
	            currentRow = new ArrayList<>();
	            rows.add(currentRow);
	        }
	        currentRow.add(eventVO);
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
	    List<List<EventVO>> rows = new ArrayList<>();
	    List<EventVO> currentRow = null;

	    for (Event event : eventPage.getContent()) {
		    String state =  calculateEventStatus(event);
		    EventVO eventVO = changeEventtoevenvo(event);
		    eventVO.setEventState(state);
		    
	        if (currentRow == null || currentRow.size() >= 4) {
	            currentRow = new ArrayList<>();
	            rows.add(currentRow);
	        }
	        currentRow.add(eventVO);
	    }

	    model.addAttribute("rows", rows);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", eventPage.getTotalPages());
	}
	
	//연극 행사리스트
	@GetMapping("/event/playlist")
	public void playlist(Model model, @RequestParam(defaultValue = "1") int page) {
		int pageSize = 16; // 한 페이지에 보여줄 개수 (4열 4행이므로 16개씩)
	    Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by("eventno").descending());
	    Page<Event> eventPage = eventdao_jpa.findByCategoryno(4, pageable); // categoryno가 2인 데이터만 가져오도록 변경
	    List<List<EventVO>> rows = new ArrayList<>();
	    List<EventVO> currentRow = null;

	    for (Event event : eventPage.getContent()) {
		    String state =  calculateEventStatus(event);
		    EventVO eventVO = changeEventtoevenvo(event);
		    eventVO.setEventState(state);
		    
	        if (currentRow == null || currentRow.size() >= 4) {
	            currentRow = new ArrayList<>();
	            rows.add(currentRow);
	        }
	        currentRow.add(eventVO);
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
	    List<List<EventVO>> rows = new ArrayList<>();
	    List<EventVO> currentRow = null;

	    for (Event event : eventPage.getContent()) {
		    String state =  calculateEventStatus(event);
		    EventVO eventVO = changeEventtoevenvo(event);
		    eventVO.setEventState(state);
		    
	        if (currentRow == null || currentRow.size() >= 4) {
	            currentRow = new ArrayList<>();
	            rows.add(currentRow);
	        }
	        currentRow.add(eventVO);
	    }

	    model.addAttribute("rows", rows);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", eventPage.getTotalPages());
	}
	
//	행사 상세로 이동
//	@GetMapping("/event/eventdetail")
//	public void eventDetail() {
//	}
}
