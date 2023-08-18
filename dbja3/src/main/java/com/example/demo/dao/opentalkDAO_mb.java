package com.example.demo.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.db.DBManager;
import com.example.demo.entity.Opentalk;
import com.example.demo.vo.OpentalkVO;

@Repository
public class opentalkDAO_mb {
	
	public List<OpentalkVO> findAllTalk(){
		List<OpentalkVO> list=null;
		list=DBManager.findAllTalk();
		
		for(OpentalkVO vo:list) {
			String day=vo.getDay();
			int hour=Integer.parseInt(day.substring(0, day.indexOf(":")));
			int min=Integer.parseInt(day.substring(day.indexOf(":")+1,day.length()));
			
			if(hour>12) {
				hour-=12;
				day="오후"+hour+""+":"+min+"";
			}else {
				day="오전"+hour+""+":"+min+"";
			}
			//System.out.println(day);
			vo.setDay(day);
			
			String fname=vo.getFname();
			
			vo.setFname(fname);
		}
		return list;
	}
	public List<OpentalkVO> findByNo(HashMap<String, Integer> map){
		List<OpentalkVO> list=null;
		list=DBManager.findByNo(map);
		
		for(OpentalkVO vo:list) {
			String day=vo.getDay();
			int hour=Integer.parseInt(day.substring(0, day.indexOf(":")));
			int min=Integer.parseInt(day.substring(day.indexOf(":")+1,day.length()));
			
			if(hour>12) {
				hour-=12;
				day="오후"+hour+""+":"+min+"";
			}else {
				day="오전"+hour+""+":"+min+"";
			}
			//System.out.println(day);
			vo.setDay(day);
			String fname=vo.getFname();
			
			vo.setFname(fname);
		}
		System.out.println("list"+list);
		return list;
	}
}
