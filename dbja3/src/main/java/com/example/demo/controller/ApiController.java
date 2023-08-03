package com.example.demo.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.service.ApiService;

import lombok.Setter;

@Controller
@Setter
public class ApiController {
	
	@Autowired
	private ApiService as;

	// service 선언
	
	@GetMapping("/EventApi")
	// localhost:8080/api 호출 시 실행
	public void callFestivalApi() throws IOException {
		for(int i=1; i<= ApiService.size; i++) {
			// service에서 선언한 80번 반복
			StringBuilder result = new StringBuilder();
			String urlStr = "http://api.data.go.kr/openapi/tn_pubr_public_pblprfr_event_info_api?"
					+ "serviceKey=VWjrKOplh6WKUDOtjAXJg%2B4VF%2Fyy8ff2zTj7s6lpqB6aESRmFWR6LDKoK0M%2FyWcOBXEP0taEu3im95J47VQ%2BfA%3D%3D&"
					+ "pageNo="+i+"&numOfRows=100&type=json";
			// 페이지번호를 바꿔주며 값 가져오기
			URL url = new URL(urlStr);
			
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			
			BufferedReader br;
			br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
			
			String returnLine;
			while((returnLine = br.readLine()) != null) {
				result.append(returnLine+"\n\r");
				// 받아온 json을 result에 담음.
			}
			
			urlConnection.disconnect(); 
			// 연결 종료
			as.init(result.toString());
			// Service로 값 넘겨줌.
			System.out.println(i+"번째 컨트롤러 동작 완료");
		}
	}
	
}
