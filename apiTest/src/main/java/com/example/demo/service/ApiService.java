package com.example.demo.service;

import java.sql.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.FestivalDAO;
import com.example.demo.vo.FestivalVO;

import lombok.Setter;

@Service
@Setter
public class ApiService {

	@Autowired
	private FestivalDAO fdao;

	@Autowired
	public static int size = 70;

	public void init(String jsonData) {

		// System.out.println("service 작동");
		try {
			JSONObject jObj;
			// json 객체 생성
			JSONParser jsonParser = new JSONParser();
			// json 파싱 객체 생성
			JSONObject jsonObj = (JSONObject) jsonParser.parse(jsonData);
			// controller 에서 가져온 값을 json객체를 통해 저장

			// 데이터 분해
			JSONObject parseResponse = (JSONObject) jsonObj.get("response");
			System.out.println("parseResponse: " + parseResponse);
			JSONObject parseBody = (JSONObject) parseResponse.get("body");
			System.out.println("parseBody: " + parseBody);

			// response 내부의 body에 데이터가 있음
			JSONArray array = (JSONArray) parseBody.get("items");
			// items 내부의 data는 배열의 형태로 받음

			
			  for(int i=0; i<array.size(); i++) { // array의 size만큼 반복문돌며 data를 하나하나 추출함
			  jObj = (JSONObject) array.get(i); FestivalVO f = new FestivalVO();
			  f.setEventNm((String)jObj.get("eventNm"));
			  f.setOpar((String)jObj.get("opar"));
			  
			  String eventCo = (String) jObj.get("eventCo")+" 할인정보: "+(String)
			  jObj.get("dscntInfo") +" 유의사항: "+(String) jObj.get("atpn");
			  
			  f.setEventCo((String) jObj.get("eventCo")); f.setEventStartDate((String)
			  jObj.get("eventStartDate")); f.setEventEndDate((String)
			  jObj.get("eventEndDate")); f.setPhoneNumber((String)
			  jObj.get("phoneNumber")); f.setHomepageUrl((String) jObj.get("homepageUrl"));
			  f.setAdvantkInfo((String) jObj.get("advantkInfo")); f.setPrkplceYn((String)
			  jObj.get("prkplceYn")); f.setRdnmadr((String) jObj.get("rdnmadr"));
			  f.setLatitude((String) jObj.get("latitude")); f.setLongitude((String)
			  jObj.get("longitude")); // System.out.println(f); fdao.save(f); 
			  //FestivalVO에 값을 넣어 insert 
			  }
			 

		} catch (Exception e) {
			System.out.println("예외: " + e.getMessage());
		}
	}
}
