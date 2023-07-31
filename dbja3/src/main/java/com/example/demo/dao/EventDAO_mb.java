package com.example.demo.dao;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.example.demo.db.DBManager;
import com.example.demo.vo.EventVO;


@Repository
public class EventDAO_mb {
	//캘린더 공연일정
	public List<Map<String, Object>> getEventList(){
        List<Map<String, Object>> eventList = new ArrayList<Map<String, Object>>();
        //전체 행사 목록
        List<EventVO> list=new ArrayList<>();
        list=DBManager.findEvent();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for(EventVO v:list) {
        	if(v.getEventstart()==null||v.getEventend()==null) {
        		continue;
        	}
        	Map<String, Object> event = new HashMap<String, Object>();
        	event.put("title",v.getEventname() );
        	event.put("start",dateFormat.format(v.getEventstart()));
        	event.put("end", dateFormat.format(v.getEventend()));
        	event.put("textcolor", "#000000");
        	int num=v.getCategoryno();
        	if(num==1) {
        		event.put("color", "rgb(254 116 151 / 60%)");
        	}else if(num==2) {
        		event.put("color", "rgb(2 180 203 / 50%)");
        	}else if(num==3) {
        		event.put("color", "rgb(7 180 127 / 50%)");
        	}else {
        		event.put("color", "rgb(253 208 15 / 80%)");
        	}
        	eventList.add(event);
        }
        return eventList;
	}
	//캘린더 티켓오픈일정
		public List<Map<String, Object>> getEventTicketList(){
	        List<Map<String, Object>> eventList = new ArrayList<Map<String, Object>>();
	        //전체 행사 목록
	        List<EventVO> list=new ArrayList<>();
	        list=DBManager.findEvent();
	        for(EventVO v:list) {
	        	if(v.getEventticket()==null||v.getEventticket().equals("")) {continue;}
	        	Map<String, Object> event = new HashMap<String, Object>();
	        	event.put("title",v.getEventname() );
	        	event.put("start",v.getEventticket().replaceAll("/", "-"));
	        	event.put("textcolor", "#000000");
	        	int num=v.getCategoryno();
	        	if(num==1) {
	        		event.put("color", "rgb(254 116 151 / 60%)");
	        	}else if(num==2) {
	        		event.put("color", "rgb(2 180 203 / 50%)");
	        	}else if(num==3) {
	        		event.put("color", "rgb(7 180 127 / 50%)");
	        	}else {
	        		event.put("color", "rgb(253 208 15 / 80%)");
	        	}
	        	eventList.add(event);
	        }
	        return eventList;
		}
	
	
	//행사 지역에 따른 event top15
	public List<EventVO> findByArea(String area){
		List<EventVO> list=null;
		list=DBManager.findByArea(area);
		for(EventVO vo:list) {
			// 현재 날짜와 시간을 가져옵니다.
			Date today = new Date();

			// 이벤트 시작 날짜를 가져옵니다.
			Date eventstart = vo.getEventstart();
			
			// 이벤트 시작 날짜와 현재 날짜를 비교합니다.
			int comparisonResult = eventstart.compareTo(today);
			
			if (comparisonResult < 0) {
				 vo.setEventState("공연중");
			} else if (comparisonResult > 0) {
				vo.setEventState("공연예정");
			}
	        
	        
	        String fname=vo.getEventfname();
	        if(fname==null || fname.equals("")) {
	        	fname="togetready.png";
	        	vo.setEventfname(fname);
	        }
		}
		return list;
	}
	
	//전체 event top15
	public List<EventVO> findTop(){
		List<EventVO> list=null;
		list=DBManager.findTop();
		for(EventVO vo:list) {
			// 현재 날짜와 시간을 가져옵니다.
			Date today = new Date();

			// 이벤트 시작 날짜를 가져옵니다.
			Date eventstart = vo.getEventstart();
			
			// 이벤트 시작 날짜와 현재 날짜를 비교합니다.
			int comparisonResult = eventstart.compareTo(today);
			
			if (comparisonResult < 0) {
				 vo.setEventState("공연중");
			} else if (comparisonResult > 0) {
				vo.setEventState("공연예정");
			}
	        
	        
	        String fname=vo.getEventfname();
	        if(fname==null || fname.equals("")) {
	        	fname="togetready.png";
	        	vo.setEventfname(fname);
	        }
		}
		return list;
	}
}
