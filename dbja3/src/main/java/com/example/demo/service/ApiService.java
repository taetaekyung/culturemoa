package com.example.demo.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.EventDAO_jpa;
import com.example.demo.entity.Event;

import lombok.Setter;

@Service
@Setter
public class ApiService {

	@Autowired
	private EventDAO_jpa edao;

	public static int size=80;
	public static int no; // 정적변수 선언. 다른 data를 가져올 경우를 대비
	
	public void init(String jsonData) {
		no = edao.getEventNo(); // eventno 부여
		
		try {
			JSONObject jObj;
			// json 객체 생성
			JSONParser jsonParser = new JSONParser();
			// json 파싱 객체 생성
			JSONObject jsonObj = (JSONObject) jsonParser.parse(jsonData);
			// controller 에서 가져온 값을 json객체를 통해 저장

			// 데이터 분해
			JSONObject parseResponse = (JSONObject) jsonObj.get("response");
			JSONObject parseBody = (JSONObject) parseResponse.get("body");
			// response 내부의 body에 데이터가 있음
			
			JSONArray array = (JSONArray) parseBody.get("items");
			// items 내부의 data는 배열의 형태로 받음

			
			  for(int i=0; i<array.size(); i++) { // array의 size만큼 반복문돌며 data를 하나하나 추출함
				  jObj = (JSONObject) array.get(i); 
				  Event e = new Event();
				  String name = (String)jObj.get("eventNm"); // 행사이름
				  if(edao.countByEventname(name) != 0) { // 행사이름이 이미 존재하면 건너뜀
					  continue;
				  }
				  e.setEventname(name);
				  if(name.contains("콘서트") || name.contains("공연")) { 
					  // 이름에 콘서트나 공연이 들어가면 categoryno 1 부여
					  e.setCategoryno(1);
				  }
				  else if(name.contains("뮤지컬")) {
					  // 이름에 뮤지컬이 들어가면 categoryno 3 부여
					  e.setCategoryno(3);
				  }
				  else if(name.contains("연극")) {
					  // 이름에 연극이 들어가면 categoryno 4 부여
					  e.setCategoryno(4);
				  }
				  else if(name.contains("페스티벌") || name.contains("축제")) {
					  // 이름에 페스티벌이나 축제가 들어가면 categoryno 5 부여
					  e.setCategoryno(5);
				  }
				  else {
					  continue;
					  // 이름에 카테고리와 관련된 글자가 없는 경우 pass
				  }
				  // 행사카테고리번호
				  
				  e.setEventplace((String)jObj.get("opar")); // 행사장소명
				  e.setEventhit(1); // 행사조회수
				  e.setEventcontent((String) jObj.get("eventCo"));  // 행사내용
				  String eventstart =(String) jObj.get("eventStartDate"); // 행사시작일
				  String eventend = (String)jObj.get("eventEndDate");  // 행사종료일
			      // 포맷터
			      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			      e.setEventstart(formatter.parse(eventstart));
			      e.setEventend(formatter.parse(eventend));
			      // db에 저장하는 포맷 변경
				  
				  String link = (String) jObj.get("homepageUrl");
				  if(link == null || link.equals("")) {
					  continue; // 행사 링크가 존재하지 않는 경우 넘어감
				  }
				  e.setEventlink(link);  // 행사링크
				  
				  String parkplace = (String)jObj.get("prkplceYn");
				  if(parkplace == null || parkplace.equals("")) {
					  parkplace = "N"; // 주차장이 없는 경우 N
				  }
				  e.setParkplace(parkplace);  // 주차장여부
				  
				  e.setEventaddr((String) jObj.get("rdnmadr")); // 행사도로명주소
				  e.setEventlat((String) jObj.get("latitude"));  // 행사주소경도		
				  e.setEventlong((String)jObj.get("longitude")); // 행사주소위도
				  e.setEventno(no); // 행사번호
				  no++;
				  edao.save(e); 
				  //Event에 값을 넣어 insert 
			  }

		} catch (Exception e) {
			System.out.println("예외: " + e.getMessage());
		}
	}
}
