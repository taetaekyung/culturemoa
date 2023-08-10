package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dao.BoardDAO_jpa;
import com.example.demo.dao.EventDAO_jpa;
import com.example.demo.dao.EventDAO_mb;
import com.example.demo.dao.FAQDAO_jpa;
import com.example.demo.dao.MemberDAO_jpa;
import com.example.demo.dao.ReviewBoardDAO_jpa;
import com.example.demo.dao.opentalkDAO_jpa;
import com.example.demo.dao.opentalkDAO_mb;
import com.example.demo.entity.Board;
import com.example.demo.entity.Event;
import com.example.demo.entity.Member;
import com.example.demo.entity.Opentalk;
import com.example.demo.entity.Reviewboard;
import com.example.demo.vo.BoardVO;
import com.example.demo.vo.EventVO;
import com.example.demo.vo.OpentalkVO;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
   //로그인 했을때만 이용가능 
   //로그인 했을때 아이디
   private String id="user01";
   private int nowNo; //현재 마지막 채팅 번호
   
   @Autowired
   private MemberDAO_jpa memberdao_jpa;
   @Autowired
   private opentalkDAO_jpa opentalkdao_jpa;
   @Autowired
   private opentalkDAO_mb opentalkdao_mb;   
   @Autowired
   private EventDAO_mb eventdao_mb;
   @Autowired
   private BoardDAO_jpa boarddao_jpa;
   @Autowired
   private EventDAO_jpa eventdao_jpa;
   @Autowired
   private FAQDAO_jpa faqdao_jpa;
   @Autowired
   private ReviewBoardDAO_jpa reviewdao_jpa;

   public BoardVO changeBoardVO(Board b) {
	   BoardVO bvo = new BoardVO();
	   bvo.setNo(b.getBoardno());
	   bvo.setBcategory(b.getBcategory());
	   bvo.setTitle(b.getBoardtitle());
	   bvo.setRegdate(b.getRegdate());
	   bvo.setId(b.getMemberId());
	   bvo.setHit(b.getBoardhit());
	   bvo.setLikes(b.getBoardlikes());
	   return bvo;
   }
   
   public BoardVO changeBoardVO(Reviewboard r) {
	   BoardVO bvo = new BoardVO();
	   bvo.setNo(r.getReviewno());
	   bvo.setBcategory("후기");
	   bvo.setTitle(r.getReviewtitle());
	   bvo.setRegdate(r.getRegdate());
	   bvo.setId(r.getMemberId());
	   bvo.setHit(r.getReviewhit());
	   bvo.setLikes(r.getReviewlike());
	   return bvo;
   }
   
   
   //캘린더 공연일정
   @GetMapping("/calendarEvent") 
      public @ResponseBody List<Map<String, Object>> getEvent(){
        return eventdao_mb.getEventList();
    }
   //캘린더 티켓오픈일정
   @GetMapping("/calendarTicket") 
      public @ResponseBody List<Map<String, Object>> getTicket(){
        return eventdao_mb.getEventTicketList();
 }
   
	@GetMapping("/")
	public String main() {
	   return "main";
	}

   
   //메인페이지를 열었을 때
   @GetMapping("/mainPage")
   public void mainPage(Model model, HttpSession session) {
	  //session으로 Member Entity 전달하기
	   Member m = null;
	  if(id != null && !id.equals("") && memberdao_jpa.countById(id) != 0) {
		  m = memberdao_jpa.findById(id).get();
	  }
	  session.setAttribute("m", m);
	   
	  //최신 10개 게시물
	  model.addAttribute("list", boarddao_jpa.findAll());

      
	  //System.out.println("되니?");
      //주변행사소식 행사 리스트 출력
      List<EventVO> event=null;
      event=eventdao_mb.findTop();
      ArrayList<EventVO> event1=new ArrayList<>();
      ArrayList<EventVO> event2=new ArrayList<>();
      ArrayList<EventVO> event3=new ArrayList<>();
      
      
      for(int i=0;i<15;i++) {
         if(i<5) {
            event1.add(event.get(i));
         }else if(i>=5 && i<10){
            event2.add(event.get(i));
         }else {
            event3.add(event.get(i));
         }
      }
      
      model.addAttribute("event1",event1);
      model.addAttribute("event2",event2);
      model.addAttribute("event3",event3);
      
      
      //--------------채팅창 로드
      int now=opentalkdao_jpa.nextNo()-1;
      nowNo=now;
      List<OpentalkVO> list=null;
      list=opentalkdao_mb.findAllTalk();
      model.addAttribute("talk", list);
      model.addAttribute("now", nowNo);
   }
   
   //주변행사지역 > 행사지역 선택했을 때
   @GetMapping("selectArea")
   @ResponseBody
   public ArrayList<ArrayList<EventVO>> selectArea(String area){
      ArrayList<ArrayList<EventVO>> list=new ArrayList<>();
      
      List<EventVO> event=null;
      if(area.equals("all")) {
         event=eventdao_mb.findTop();
      }else {
         event=eventdao_mb.findByArea(area);
      }
      
      ArrayList<EventVO> event1=new ArrayList<>();
      ArrayList<EventVO> event2=new ArrayList<>();
      ArrayList<EventVO> event3=new ArrayList<>();
      for(int i=0;i<event.size();i++) {
         if(i<5) {
            event1.add(event.get(i));
         }else if(i>=5 && i<10){
            event2.add(event.get(i));
         }else {
            event3.add(event.get(i));
         }
      }
      if(event1.size()!=0) {
         list.add(event1);
      }
      if(event2.size()!=0) {
         list.add(event2);
      }
      if(event3.size()!=0) {
         list.add(event3);
      }
      
      
      return list;
   }
   
   
   //------------채팅을 입력했을 때
   @GetMapping("main_insertTalk")
   @ResponseBody
   public void insertTalk(String talk) {
      int next=opentalkdao_jpa.nextNo();
      nowNo=next;  //현재 마지막 채팅 번호 업데이트
      Opentalk o=new Opentalk();
      o.setTalkcontent(talk);
      o.setTalkno(next);
      o.setMemberId(id); 
      opentalkdao_jpa.insert(o);
   }
   
   //------------채팅창 업데이트
   @GetMapping("main_updateTalk")
   @ResponseBody
   public List<OpentalkVO> updateTalk(int now){
      nowNo=opentalkdao_jpa.nextNo()-1;  //nowNo를 db에 있는 마지막 채팅 번호로 업데이트
      List<OpentalkVO> list=null;
      if(now!=nowNo) {  //채팅창에 있는 마지막 no와 db에 있는 마지막 no가 다를때 업데이트
         int start=now+1;
         int end=nowNo;
         HashMap<String, Integer> map=new HashMap<>();         
         map.put("start", start);
         map.put("end", end);
         list=opentalkdao_mb.findByNo(map);   
      }   
      return list;
      }

   
   // 헤더의 검색창에서 검색했을 때
   @PostMapping("/searchresult")
   public void searchresult(Model model, String keyword_main) {  
	   
	   
	   List<Event> event_list = eventdao_jpa.findByKeyword(keyword_main);
	   int event_list_size = event_list.size();

	//   if(event_list.size() > 4) {
	//	   event_list = event_list.subList(0, 4);
	//   }
	   
	   
	   List<Board> board_list = boarddao_jpa.findByKeyword(keyword_main);
	   List<Reviewboard> review_list = reviewdao_jpa.findByKeyword(keyword_main);
	   
	   int boardsize = board_list.size();
	   int reviewsize = review_list.size();
	   int board_list_size = boardsize + reviewsize;
	   
	   List<BoardVO> searchlist = new ArrayList<BoardVO>();
	   
	   if(boardsize != 0 && reviewsize != 0) {
		   int i = 0; // board_list의 인덱스
		   int j = 0; // review_list의 인덱스


		   while( i < boardsize && j < reviewsize) {
			   // 만약 board_list에 있는 date가 review에 있는 date보다 뒤이면 
			   if(board_list.get(i).getRegdate().after(review_list.get(j).getRegdate())) {
				   searchlist.add(changeBoardVO(board_list.get(i)));
				   i++;
			   }
			   else {
				   searchlist.add(changeBoardVO(review_list.get(j)));
				   j++;
			   }
		   }
		   if(i >= boardsize) {
			   while(j < reviewsize) {
				   searchlist.add(changeBoardVO(review_list.get(j)));
				   j++;
			   }
		   }
		   else if(j >= reviewsize) {
			   while(i < boardsize) {
				   searchlist.add(changeBoardVO(board_list.get(i)));
				   i++;
			   }
		   }
	   }
	   
	   
	   else {
		   if(boardsize != 0 && reviewsize == 0) {
			   int i = 0;
			   while(i < boardsize) {
				   searchlist.add(changeBoardVO(board_list.get(i)));
				   i++;
			   }
		   }
		   else {
			   int i = 0;
			   while(i < reviewsize) {
				   searchlist.add(changeBoardVO(review_list.get(i)));
				   i++;
			   }
		   }
	   }
	   
	   
	   model.addAttribute("keyword_main", keyword_main);
	   model.addAttribute("board_list_size", board_list_size);
	   model.addAttribute("event_list_size", event_list_size);
	   model.addAttribute("event_list", event_list);
	   model.addAttribute("list", searchlist);
   }
   
   
}