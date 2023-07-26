package com.example.demo.controller;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.EventDAO_jpa;
import com.example.demo.entity.Event;

@RestController
public class JsoupController {
	@Autowired
	private EventDAO_jpa event_jpa;
	
	@GetMapping("/img")
	public void newBook(){
		ArrayList<String>tlist=new ArrayList<>();
		ArrayList<String> flist=new ArrayList<>();
		ArrayList<Event> arr=new ArrayList<>();
		for(int p=1;p<=12;p++) {
		String url="https://festivallife.kr/concert_k/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6MTA7fQ%3D%3D&page="+p+"";
			try {
				Document doc=Jsoup.connect(url).get(); //jsoup이 url와 연결해서 doucument형태로 가져온다
				Elements list=doc.getElementsByClass("title title-block"); //url주소의 book_tit가져와
				for(Element e:list) {
					Element a=e.getElementsByTag("div").get(0); //자식이 "a"태그를 찾아주세요 근데 무조건 배열로 주니 첫번째 
					String title=a.text();	//그 책의 이름
					title=title.substring(title.indexOf(" ")+1, title.length());
					if(title.indexOf("내한")!=-1) {
						title=title.substring(0, title.indexOf("내한"));
					}
					System.out.println(title);
					tlist.add(title);
				}
				
				Elements cardWrappers = doc.select("[style*=background-image][class~=card_wrapper]");
				for (Element wrapper : cardWrappers) {
				    String style = wrapper.attr("style");
				    String regex = "url\\((.*?)\\)";
				    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
				    java.util.regex.Matcher matcher = pattern.matcher(style);
	
				    // 배경 이미지 URL이 매칭되었다면 출력합니다.
				    if (matcher.find()) {
				        String backgroundImageURL = matcher.group(1);
				        flist.add(backgroundImageURL);
				    } else {
				        System.out.println("No match");
				    }
				}
				
			} catch (Exception e) {
				System.out.println("크로릴예외발생:"+e.getMessage());
				}
		}
		//fname과 title을 짝지어 db에 넣기
		System.out.println(tlist.size());
		System.out.println(flist.size());
		
		for(int i=0;i<flist.size();i++) {
			String addr=flist.get(i);
			String eventName=tlist.get(i);
			String eventFname=eventName+".jpg";
			
			try {
				URL url2=new URL(addr);
				InputStream is=url2.openStream();
				FileOutputStream fos=new FileOutputStream("c:/data/"+eventFname);
				FileCopyUtils.copy(is.readAllBytes(), fos);
				is.close();
				fos.close();
				Event event=new Event();
				event.setEventfname(eventFname);
				event.setEventhit(1);
				event.setCategoryno(2);
				event.setEventname(eventName);
				//event.setEventno(i+51);
				//event_jpa.save(event);
				
			} catch (Exception e) {
				System.out.println("insert예외발생:"+e.getMessage());
			}
		}
	}
}
