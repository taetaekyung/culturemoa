package com.example.demo.controller;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Event;

@RestController
public class JsoupController {
	@GetMapping("/img")
	public ArrayList<Event> newBook(){
		ArrayList<Event> arr=new ArrayList<>();
		String url="https://festivallife.kr/concert_k";
		try {
			Document doc=Jsoup.connect(url).get(); //jsoup이 url와 연결해서 doucument형태로 가져온다
			
			
			Elements list=doc.getElementsByClass("title title-block"); //url주소의 book_tit가져와
			for(Element e:list) {
				Element a=e.getElementsByTag("div").get(0); //자식이 "a"태그를 찾아주세요 근데 무조건 배열로 주니 첫번째 
				String title=a.text();	//그 책의 이름
				title=title.substring(title.indexOf(" ")+1, title.length());
				System.out.println("title:"+title);
				
				String fname=title+".jpg";
				
			
				//arr.add(new NewBook(title,link)); //responsebody에 있으면 자동으로 list를 jsop으로 바꿔준다
			}
				
			
		} catch (Exception e) {
			System.out.println("예외발생:"+e.getMessage());
		}
		return arr;
	}
}
