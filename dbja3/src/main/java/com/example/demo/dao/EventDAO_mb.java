package com.example.demo.dao;


import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.db.DBManager;
import com.example.demo.vo.EventVO;


@Repository
public class EventDAO_mb {
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
