package com.example.demo.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.db.DBManager;
import com.example.demo.vo.EventVO;
import com.example.demo.vo.EventVO2;

@Repository
public class EventDAO_mb {
	//전체 event top15
	public List<EventVO2> findTop(){
		List<EventVO2> list=null;
		list=DBManager.findTop();
		for(EventVO2 vo:list) {
			String eventstart=vo.getEventstart().toString();
			String []day=eventstart.split("-");
			// 현재 날짜 가져오기
	        LocalDate currentDate = LocalDate.now();
	        
	        // 비교할 날짜 설정 (2023년 7월 16일)
	        LocalDate targetDate = LocalDate.of(Integer.parseInt(day[0]),Integer.parseInt(day[1]),Integer.parseInt(day[2]));
	        // 날짜 비교
	        if (currentDate.isBefore(targetDate)) {
	            vo.setEventState("공연예정");
	        } else {
	            vo.setEventState("공연중");
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
