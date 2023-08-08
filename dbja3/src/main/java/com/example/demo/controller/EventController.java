package com.example.demo.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dao.EventDAO_jpa;
import com.example.demo.dao.EventDAO_mb;
import com.example.demo.dao.WishListDAO_jpa;
import com.example.demo.entity.Event;
import com.example.demo.entity.Member;
import com.example.demo.entity.Wishlist;
import com.example.demo.vo.EventVO;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.Setter;

@Controller
@Setter
public class EventController {
	
	@Autowired
	private EventDAO_jpa eventdao_jpa;
	@Autowired
	private EventDAO_mb eventdao_mb;
	@Autowired
	private WishListDAO_jpa wishlistdao_jpa;
	//오늘 날짜
	LocalDate currentDate = LocalDate.now();
	
	//event 엔티티 eventvo 로 이동. 공연 진행 상태 추가해서 쓰려고
	public EventVO changeEventtoeventvo(Event event) {
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
	
    // 행사 상세 : <이벤트 번호>를 받아서 <전체 이벤트 정보>와 <공연진행상태>를 반환함.
    @GetMapping("/event/eventdetail")
    @Transactional // 추가: 조회수 업데이트를 위해 트랜잭션을 사용
    public String eventDetail(HttpSession session, @RequestParam int eventno, Model model) {
    	
    	String id = ((Member)session.getAttribute("m")).getId();
    	
    	//시간 생략을 위한 Formatter
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        
    	// eventno를 사용하여 해당 이벤트를 조회합니다. (여기에서는 이벤트를 찾는 로직을 추가해야 합니다.)
        Event event = eventdao_jpa.findByEventno(eventno);
        
        if(event.getEventstart() != null) {
            String eventStart = formatter.format(event.getEventstart());
            model.addAttribute("eventStart", eventStart);
        }
        else {
            model.addAttribute("eventStart", null);
        }
        
        if(event.getEventend() != null) {
            String eventEnd = formatter.format(event.getEventend());
            model.addAttribute("eventEnd", eventEnd);
        }
        else {
            model.addAttribute("eventEnd", null);
        }
        
        String eventlink = event.getEventlink();
        if (eventlink != null && !eventlink.startsWith("http")) {
            // Add 'http://' to the eventlink if it doesn't start with 'http'
            eventlink = "http://" + eventlink;
        }
        event.setEventlink(eventlink);
        
        String state = calculateEventStatus(event);

        // 조회수를 1 증가시킵니다.
        event.setEventhit(event.getEventhit() + 1); // 이벤트의 조회수를 1 증가시킵니다.
        eventdao_jpa.save(event); // 변경된 이벤트 엔티티를 저장합니다.
        
        int cnt = wishlistdao_jpa.countByIdAndEventno(id, eventno);
        
        
        model.addAttribute("cnt", cnt);
        model.addAttribute("event", event);
        model.addAttribute("state", state);
        return "/event/eventdetail"; // "/event/eventdetail" 페이지로 이동하도록 반환합니다.
    }
	/*
	@GetMapping("/event/domesticconcertlist")
	public void domesticconcertlist(Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String area, @RequestParam(required = false) String eventstate) {
	    int pageSize = 16;
	    Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("eventno").descending());

	    Page<Event> eventPage;

	    List<List<EventVO>> rows = new ArrayList<>();
	    List<EventVO> currentRow = null;
	    String state="";
	    for (Event event : eventPage.getContent()) {
	        state = calculateEventStatus(event);
	        EventVO eventVO = changeEventtoeventvo(event);
	        eventVO.setEventState(state);

	        if (currentRow == null || currentRow.size() >= 4) {
	            currentRow = new ArrayList<>();
	            rows.add(currentRow);
	        }
	        currentRow.add(eventVO);
	    }
	    
	    if (area != null && !area.isEmpty() && !eventstate.isEmpty()) {
	        if (area.equals("all")) {
	            // 전체 지역과 공연상태 선택 시 해당 카테고리의 행사 리스트를 가져옴
	            eventPage = eventdao_jpa.findByCategorynoAndEventstate(1, state, pageable);
	        } else {
	            // 선택한 지역과 공연상태가 모두 있는 경우 해당 지역과 공연상태에 맞는 행사 리스트를 가져옴
	            eventPage = eventdao_jpa.findByEventaddrContainingAndEventstateAndCategoryno(area, eventstate, 1, pageable);
	        }
	    } else if (area != null && !area.isEmpty()) {
	        if (area.equals("all")) {
	            // 전체 지역 선택 시 해당 카테고리의 행사 리스트를 가져옴
	            eventPage = eventdao_jpa.findByCategoryno(1, pageable);
	        } else {
	            // 선택한 지역이 있으면 해당 지역의 행사 리스트를 가져옴
	            eventPage = eventdao_jpa.findByEventaddrContainingAndCategoryno(area, 1, pageable);
	        }
	    } else if (!eventstate.isEmpty()) {
	        // 선택한 지역이 없지만 공연상태만 선택한 경우 해당 공연상태에 맞는 행사 리스트를 가져옴
	        eventPage = eventdao_jpa.findByEventstateAndCategoryno(eventstate, 1, pageable);
	    } else {
	        // 선택한 지역과 공연상태가 없는 경우 전체 행사 리스트를 가져옴
	        eventPage = eventdao_jpa.findByCategoryno(1, pageable);
	    }


	    model.addAttribute("rows", rows);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", eventPage.getTotalPages());
	}
	
	
	
	//국내공연 :<페이지번호,지역>매개변수 받아서<행사정보,현재 페이지,전체 페이지 개수>반환 
	@GetMapping("/event/domesticconcertlist")
	public void domesticconcertlist(Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String area) {
	    int pageSize = 16;
	    Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("eventno").descending());

	    Page<Event> eventPage;
	    if (area != null && !area.isEmpty()) {
	    	if(area.equals("all")) { //전체 클릭하면 카테고리만 받아서 전체 출력
	    		eventPage = eventdao_jpa.findByCategoryno(1, pageable);	
	    	}else { // 선택한 지역이 있으면 해당 지역의 행사 리스트를 가져오도록 변경
	    		eventPage = eventdao_jpa.findByEventaddrContainingAndCategoryno(area, 1, pageable);
	    	}
	    } else {
	        // 선택한 지역이 없으면 전체 행사 리스트를 가져옴
	        eventPage = eventdao_jpa.findByCategoryno(1, pageable);
	    }

	    List<List<EventVO>> rows = new ArrayList<>();
	    List<EventVO> currentRow = null;

	    for (Event event : eventPage.getContent()) {
	        String state = calculateEventStatus(event);
	        EventVO eventVO = changeEventtoeventvo(event);
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
	*/
  //국내공연 :<페이지번호,지역>매개변수 받아서<행사정보,현재 페이지,전체 페이지 개수>반환 
  	@GetMapping("/event/domesticconcertlist")
  	public void domesticconcertlist(Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String eventArea, @RequestParam(required = false) String eventDate) throws ParseException {
  	    int pageSize = 16;
  	    Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("eventno").descending());

  	    Page<Event> eventPage = null;
  	    model.addAttribute("eventArea", "지역을 선택하세요");
  	    
  	    if (eventArea != null && !eventArea.isEmpty()) {
  	    	if(eventArea.equals("all")) { //전체 클릭하면 카테고리만 받아서 전체 출력
  	    		if(eventDate!=null&&!eventDate.equals("all")) { // //지역x, 행사날짜o
  	    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  	    		    Date parsedEventDate = sdf.parse(eventDate);
  		    		eventPage=eventdao_jpa.findByEventDateContainingAndCategoryNo(parsedEventDate, 1, pageable);
  		    		model.addAttribute("eventDate", eventDate);
  	    			
  	    		}else {//지역x, 행사날짜x
  	    			System.out.println("2번");
  	    			eventPage = eventdao_jpa.findByCategoryno(1, pageable);	
  	    			
  	    		}
  	    	}else { // 선택한 지역이 있으면 해당 지역의 행사 리스트를 가져오도록 변경
  	    		if(eventDate!=null&&!eventDate.equals("all")&&!eventDate.equals("날짜")) {//지역o, 행사날짜o
  	    			System.out.println("3번");
  	    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  	    		    Date parsedEventDate = sdf.parse(eventDate);
  		    		eventPage=eventdao_jpa.findByEventDateAndAreaContainingAndCategoryNo(parsedEventDate, eventArea, 1, pageable);
  		    		model.addAttribute("eventDate", eventDate);
  		    		model.addAttribute("eventArea", eventArea);
  	    			
  	    		}else { //지역o, 행사날짜x
  	    			System.out.println("4번");
  	    			eventPage = eventdao_jpa.findByEventaddrContainingAndCategoryno(eventArea, 1, pageable);
  		    		model.addAttribute("eventArea", eventArea);
  	    			
  	    		}
  	    		
  	    	}
  	    } else {
  	    	eventPage = eventdao_jpa.findByCategoryno(1, pageable);
  	    }
  	    


  	    List<List<EventVO>> rows = new ArrayList<>();
  	    List<EventVO> currentRow = null;
  	    
  	   
  	    
  	    for (Event event : eventPage.getContent()) {
  	    
  	        String state = calculateEventStatus(event);
  	        EventVO eventVO = changeEventtoeventvo(event);
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
  	
	//내한공연 :<페이지번호,지역>매개변수 받아서<행사정보,현재 페이지,전체 페이지 개수>반환 
  	@GetMapping("/event/koreaconcertlist")
  	public void koreaconcertlist(Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String eventArea, @RequestParam(required = false) String eventDate) throws ParseException {
  	    int pageSize = 16;
  	    Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("eventno").descending());

  	    Page<Event> eventPage = null;
  	    model.addAttribute("eventArea", "지역을 선택하세요");
  	    
  	    if (eventArea != null && !eventArea.isEmpty()) {
  	    	if(eventArea.equals("all")) { //전체 클릭하면 카테고리만 받아서 전체 출력
  	    		if(eventDate!=null&&!eventDate.equals("all")) { // //지역x, 행사날짜o
  	    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  	    		    Date parsedEventDate = sdf.parse(eventDate);
  		    		eventPage=eventdao_jpa.findByEventDateContainingAndCategoryNo(parsedEventDate, 2, pageable);
  		    		model.addAttribute("eventDate", eventDate);
  	    			
  	    		}else {//지역x, 행사날짜x
  	    			System.out.println("2번");
  	    			eventPage = eventdao_jpa.findByCategoryno(2, pageable);	
  	    			
  	    		}
  	    	}else { // 선택한 지역이 있으면 해당 지역의 행사 리스트를 가져오도록 변경
  	    		if(eventDate!=null&&!eventDate.equals("all")&&!eventDate.equals("날짜")) {//지역o, 행사날짜o
  	    			System.out.println("3번");
  	    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  	    		    Date parsedEventDate = sdf.parse(eventDate);
  		    		eventPage=eventdao_jpa.findByEventDateAndAreaContainingAndCategoryNo(parsedEventDate, eventArea, 2, pageable);
  		    		model.addAttribute("eventDate", eventDate);
  		    		model.addAttribute("eventArea", eventArea);
  	    			
  	    		}else { //지역o, 행사날짜x
  	    			System.out.println("4번");
  	    			eventPage = eventdao_jpa.findByEventaddrContainingAndCategoryno(eventArea, 2, pageable);
  		    		model.addAttribute("eventArea", eventArea);
  	    			
  	    		}
  	    		
  	    	}
  	    } else {
  	    	eventPage = eventdao_jpa.findByCategoryno(2, pageable);
  	    }
  	    


  	    List<List<EventVO>> rows = new ArrayList<>();
  	    List<EventVO> currentRow = null;
  	    
  	   
  	    
  	    for (Event event : eventPage.getContent()) {
  	    
  	        String state = calculateEventStatus(event);
  	        EventVO eventVO = changeEventtoeventvo(event);
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
  //뮤지컬 :<페이지번호,지역>매개변수 받아서<행사정보,현재 페이지,전체 페이지 개수>반환 
  	@GetMapping("/event/musicallist")
  	public void musicallist(Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String eventArea, @RequestParam(required = false) String eventDate) throws ParseException {
  	    int pageSize = 16;
  	    Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("eventno").descending());

  	    Page<Event> eventPage = null;
  	    model.addAttribute("eventArea", "지역을 선택하세요");
  	    
  	    if (eventArea != null && !eventArea.isEmpty()) {
  	    	if(eventArea.equals("all")) { //전체 클릭하면 카테고리만 받아서 전체 출력
  	    		if(eventDate!=null&&!eventDate.equals("all")) { // //지역x, 행사날짜o
  	    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  	    		    Date parsedEventDate = sdf.parse(eventDate);
  		    		eventPage=eventdao_jpa.findByEventDateContainingAndCategoryNo(parsedEventDate, 3, pageable);
  		    		model.addAttribute("eventDate", eventDate);
  	    			
  	    		}else {//지역x, 행사날짜x
  	    			System.out.println("2번");
  	    			eventPage = eventdao_jpa.findByCategoryno(3, pageable);	
  	    			
  	    		}
  	    	}else { // 선택한 지역이 있으면 해당 지역의 행사 리스트를 가져오도록 변경
  	    		if(eventDate!=null&&!eventDate.equals("all")&&!eventDate.equals("날짜")) {//지역o, 행사날짜o
  	    			System.out.println("3번");
  	    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  	    		    Date parsedEventDate = sdf.parse(eventDate);
  		    		eventPage=eventdao_jpa.findByEventDateAndAreaContainingAndCategoryNo(parsedEventDate, eventArea, 3, pageable);
  		    		model.addAttribute("eventDate", eventDate);
  		    		model.addAttribute("eventArea", eventArea);
  	    			
  	    		}else { //지역o, 행사날짜x
  	    			System.out.println("4번");
  	    			eventPage = eventdao_jpa.findByEventaddrContainingAndCategoryno(eventArea, 3, pageable);
  		    		model.addAttribute("eventArea", eventArea);
  	    			
  	    		}
  	    		
  	    	}
  	    } else {
  	    	eventPage = eventdao_jpa.findByCategoryno(3, pageable);
  	    }
  	    


  	    List<List<EventVO>> rows = new ArrayList<>();
  	    List<EventVO> currentRow = null;
  	    
  	   
  	    
  	    for (Event event : eventPage.getContent()) {
  	    
  	        String state = calculateEventStatus(event);
  	        EventVO eventVO = changeEventtoeventvo(event);
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
  //연극 :<페이지번호,지역>매개변수 받아서<행사정보,현재 페이지,전체 페이지 개수>반환 
  	@GetMapping("/event/playlist")
  	public void playlist(Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String eventArea, @RequestParam(required = false) String eventDate) throws ParseException {
  	    int pageSize = 16;
  	    Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("eventno").descending());

  	    Page<Event> eventPage = null;
  	    model.addAttribute("eventArea", "지역을 선택하세요");
  	    
  	    if (eventArea != null && !eventArea.isEmpty()) {
  	    	if(eventArea.equals("all")) { //전체 클릭하면 카테고리만 받아서 전체 출력
  	    		if(eventDate!=null&&!eventDate.equals("all")) { // //지역x, 행사날짜o
  	    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  	    		    Date parsedEventDate = sdf.parse(eventDate);
  		    		eventPage=eventdao_jpa.findByEventDateContainingAndCategoryNo(parsedEventDate, 4, pageable);
  		    		model.addAttribute("eventDate", eventDate);
  	    			
  	    		}else {//지역x, 행사날짜x
  	    			System.out.println("2번");
  	    			eventPage = eventdao_jpa.findByCategoryno(4, pageable);	
  	    			
  	    		}
  	    	}else { // 선택한 지역이 있으면 해당 지역의 행사 리스트를 가져오도록 변경
  	    		if(eventDate!=null&&!eventDate.equals("all")&&!eventDate.equals("날짜")) {//지역o, 행사날짜o
  	    			System.out.println("3번");
  	    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  	    		    Date parsedEventDate = sdf.parse(eventDate);
  		    		eventPage=eventdao_jpa.findByEventDateAndAreaContainingAndCategoryNo(parsedEventDate, eventArea, 4, pageable);
  		    		model.addAttribute("eventDate", eventDate);
  		    		model.addAttribute("eventArea", eventArea);
  	    			
  	    		}else { //지역o, 행사날짜x
  	    			System.out.println("4번");
  	    			eventPage = eventdao_jpa.findByEventaddrContainingAndCategoryno(eventArea, 4, pageable);
  		    		model.addAttribute("eventArea", eventArea);
  	    			
  	    		}
  	    		
  	    	}
  	    } else {
  	    	eventPage = eventdao_jpa.findByCategoryno(4, pageable);
  	    }
  	    


  	    List<List<EventVO>> rows = new ArrayList<>();
  	    List<EventVO> currentRow = null;
  	    
  	   
  	    
  	    for (Event event : eventPage.getContent()) {
  	    
  	        String state = calculateEventStatus(event);
  	        EventVO eventVO = changeEventtoeventvo(event);
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
  //페스티벌 :<페이지번호,지역>매개변수 받아서<행사정보,현재 페이지,전체 페이지 개수>반환 
  	@GetMapping("/event/festivallist")
  	public void festivallist(Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String eventArea, @RequestParam(required = false) String eventDate) throws ParseException {
  	    int pageSize = 16;
  	    Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("eventno").descending());

  	    Page<Event> eventPage = null;
  	    model.addAttribute("eventArea", "지역을 선택하세요");
  	    if (eventArea != null && !eventArea.isEmpty()) {
  	    	if(eventArea.equals("all")) { //전체 클릭하면 카테고리만 받아서 전체 출력
  	    		if(eventDate!=null&&!eventDate.equals("all")) { // //지역x, 행사날짜o
  	    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  	    		    Date parsedEventDate = sdf.parse(eventDate);
  		    		eventPage=eventdao_jpa.findByEventDateContainingAndCategoryNo(parsedEventDate, 5, pageable);
  		    		model.addAttribute("eventDate", eventDate);
  	    			
  	    		}else {//지역x, 행사날짜x
  	    			System.out.println("2번");
  	    			eventPage = eventdao_jpa.findByCategoryno(5, pageable);	
  	    			
  	    		}
  	    	}else { // 선택한 지역이 있으면 해당 지역의 행사 리스트를 가져오도록 변경
  	    		if(eventDate!=null&&!eventDate.equals("all")&&!eventDate.equals("날짜")) {//지역o, 행사날짜o
  	    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  	    		    Date parsedEventDate = sdf.parse(eventDate);
  		    		eventPage=eventdao_jpa.findByEventDateAndAreaContainingAndCategoryNo(parsedEventDate, eventArea, 5, pageable);
  		    		model.addAttribute("eventDate", eventDate);
  		    		model.addAttribute("eventArea", eventArea);
  	    			
  	    		}else { //지역o, 행사날짜x
  	    			eventPage = eventdao_jpa.findByEventaddrContainingAndCategoryno(eventArea, 5, pageable);
  		    		model.addAttribute("eventArea", eventArea);
  	    			
  	    		}
  	    		
  	    	}
  	    } else {
  	    	eventPage = eventdao_jpa.findByCategoryno(5, pageable);
  	    }
  	    
  	    List<List<EventVO>> rows = new ArrayList<>();
  	    List<EventVO> currentRow = null;
  	    
  	    for (Event event : eventPage.getContent()) {
  	    
  	        String state = calculateEventStatus(event);
  	        EventVO eventVO = changeEventtoeventvo(event);
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
  	/*
	@GetMapping("/event/koreaconcertlist")
	public void koreaconcertlist(Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String area) {
	    int pageSize = 16;
	    Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("eventno").descending());

	    Page<Event> eventPage;
	    if (area != null && !area.isEmpty()) {
	    	if(area.equals("all")) { //전체 클릭하면 카테고리만 가지고 전체 출력
	    		eventPage = eventdao_jpa.findByCategoryno(2, pageable);	
	    	}else { // 선택한 지역이 있으면 해당 지역의 행사 리스트를 가져오도록 변경
	    		eventPage = eventdao_jpa.findByEventaddrContainingAndCategoryno(area, 2, pageable);
	    	}
	    } else {
	        // 선택한 지역이 없으면 전체 행사 리스트를 가져옴
	        eventPage = eventdao_jpa.findByCategoryno(2, pageable);
	    }

	    List<List<EventVO>> rows = new ArrayList<>();
	    List<EventVO> currentRow = null;

	    for (Event event : eventPage.getContent()) {
	        String state = calculateEventStatus(event);
	        EventVO eventVO = changeEventtoeventvo(event);
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
	
	
	//뮤지컬 :<페이지번호,지역>매개변수 받아서<행사정보,현재 페이지,전체 페이지 개수>반환 
	@GetMapping("/event/musicallist")
	public void musicallist(Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String area) {
	    int pageSize = 16;
	    Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("eventno").descending());

	    Page<Event> eventPage;
	    if (area != null && !area.isEmpty()) {
	    	if(area.equals("all")) { //전체 클릭하면 카테고리만 가지고 전체 출력
	    		eventPage = eventdao_jpa.findByCategoryno(3, pageable);	
	    	}else { // 선택한 지역이 있으면 해당 지역의 행사 리스트를 가져오도록 변경
	    		eventPage = eventdao_jpa.findByEventaddrContainingAndCategoryno(area, 3, pageable);
	    	}
	    } else {
	        // 선택한 지역이 없으면 전체 행사 리스트를 가져옴
	        eventPage = eventdao_jpa.findByCategoryno(3, pageable);
	    }

	    List<List<EventVO>> rows = new ArrayList<>();
	    List<EventVO> currentRow = null;

	    for (Event event : eventPage.getContent()) {
	        String state = calculateEventStatus(event);
	        EventVO eventVO = changeEventtoeventvo(event);
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
	
	//연극 :<페이지번호,지역>매개변수 받아서<행사정보,현재 페이지,전체 페이지 개수>반환 
	@GetMapping("/event/playlist")
	public void playlist(Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String area) {
	    int pageSize = 16;
	    Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("eventno").descending());

	    Page<Event> eventPage;
	    if (area != null && !area.isEmpty()) {
	    	if(area.equals("all")) { //전체 클릭하면 카테고리만 가지고 전체 출력
	    		eventPage = eventdao_jpa.findByCategoryno(4, pageable);	
	    	}else { // 선택한 지역이 있으면 해당 지역의 행사 리스트를 가져오도록 변경
	    		eventPage = eventdao_jpa.findByEventaddrContainingAndCategoryno(area, 4, pageable);
	    	}
	    } else {
	        // 선택한 지역이 없으면 전체 행사 리스트를 가져옴
	        eventPage = eventdao_jpa.findByCategoryno(4, pageable);
	    }

	    List<List<EventVO>> rows = new ArrayList<>();
	    List<EventVO> currentRow = null;

	    for (Event event : eventPage.getContent()) {
	        String state = calculateEventStatus(event);
	        EventVO eventVO = changeEventtoeventvo(event);
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
	
	//페스티벌 :<페이지번호,지역>매개변수 받아서<행사정보,현재 페이지,전체 페이지 개수>반환 
	@GetMapping("/event/festivallist")
	public void festivallist(Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String area) {
	    int pageSize = 16;
	    Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("eventno").descending());

	    Page<Event> eventPage;
	    if (area != null && !area.isEmpty()) {
	    	if(area.equals("all")) { //전체 클릭하면 카테고리만 가지고 전체 출력
	    		eventPage = eventdao_jpa.findByCategoryno(5, pageable);	
	    	}else { // 선택한 지역이 있으면 해당 지역의 행사 리스트를 가져오도록 변경
	    		eventPage = eventdao_jpa.findByEventaddrContainingAndCategoryno(area, 5, pageable);
	    	}
	    } else {
	        // 선택한 지역이 없으면 전체 행사 리스트를 가져옴
	        eventPage = eventdao_jpa.findByCategoryno(5, pageable);
	    }

	    List<List<EventVO>> rows = new ArrayList<>();
	    List<EventVO> currentRow = null;

	    for (Event event : eventPage.getContent()) {
	        String state = calculateEventStatus(event);
	        EventVO eventVO = changeEventtoeventvo(event);
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
	*/
	/*
	//페스티벌 행사리스트 : <페이지 번호>를 받아서 <카테고리>에 해당하는 <행사리스트 정보>,<현재페이지번호>,<전체 페이지 개수>를 반환
	@GetMapping("/event/festivallist")
	public void festivallist(Model model, @RequestParam(defaultValue = "1") int page) {
		int pageSize = 16; // 한 페이지에 보여줄 개수 (4열 4행이므로 16개씩)
	    Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by("eventno").descending());
	    Page<Event> eventPage = eventdao_jpa.findByCategoryno(5, pageable); // categoryno가 2인 데이터만 가져오도록 변경
	    List<List<EventVO>> rows = new ArrayList<>();
	    List<EventVO> currentRow = null;

	    for (Event event : eventPage.getContent()) {
		    String state =  calculateEventStatus(event);
		    EventVO eventVO = changeEventtoeventvo(event);
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
	*/
	
	
	// 관심목록 추가/삭제
	@GetMapping("/insertLike")
	@ResponseBody
	public int insertLike(HttpSession session, int eventno) {
		int re = -1;
    	String id = ((Member)session.getAttribute("m")).getId();
        int cnt = wishlistdao_jpa.countByIdAndEventno(id, eventno);
        if(cnt == 0) {
        	re = 1;
        	Wishlist w = new Wishlist();
        	int wishno = wishlistdao_jpa.getWishNo();
        	w.setWishno(wishno+1);
        	w.setMemberId(id);
        	w.setEventEventno(eventno);
        	wishlistdao_jpa.save(w);
    		return re;
        }
        else {
        	re = 2;
        	wishlistdao_jpa.deleteByIdAndEventno(id, eventno);
        	return re;
        }

	}
}
