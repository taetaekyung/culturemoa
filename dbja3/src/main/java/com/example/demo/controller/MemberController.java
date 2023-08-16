package com.example.demo.controller;


import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dao.EventDAO_jpa;
import com.example.demo.dao.MemberDAO_jpa;
import com.example.demo.dao.MessageDAO_jpa;
import com.example.demo.entity.Event;
import com.example.demo.entity.Member;
import com.example.demo.entity.Message;
import com.example.demo.vo.EventVO;
import com.example.demo.vo.MessageVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {
	
	@Autowired
	private MemberDAO_jpa memberdao_jpa;
	
	@Autowired
	private MessageDAO_jpa messagedao_jpa;
	
	@Autowired
	private MailSender mailSender;
	
	@Autowired
	private EventDAO_jpa eventdao_jpa;
	
	// Message에 닉네임 띄우기 위해서 VO로 변환
	public MessageVO messageToMessageVO(Message m) {
		MessageVO mvo = new MessageVO();
		mvo.setMno(m.getMno());
		mvo.setMid(m.getMid());
		mvo.setMcontent(m.getMcontent());
		mvo.setId(m.getMemberId());
		mvo.setNickname(memberdao_jpa.findNicknameById(m.getMemberId()));
		mvo.setRegdate(m.getRegdate());
		return mvo;
	}
	
	
	
	/* -----------마이페이지-회원정보------------ */
	
	
	@GetMapping("/member/mypage")
	public void mypage() {
	
	}
	

	/* -----------마이페이지-쪽지함------------ */
	
	//마이페이지-답장 보내기 팝업
	@GetMapping("/member/messagereply")
	public String replyMessage(Model model,@RequestParam int mno) {
		Message message = messagedao_jpa.findById(mno).orElse(null);
		if (message != null) {
            String senderId = message.getMid();
            Optional<Member> senderOptional = memberdao_jpa.findById(senderId);
            if (senderOptional.isPresent()) {
                Member sender = senderOptional.get();
                model.addAttribute("senderNickname", sender.getNickname());
            }
            model.addAttribute("message", message);
        }
		return "/member/messagereply";
	}
	
	//마이페이지-보낸 쪽지 팝업
	@GetMapping("/member/messageboxsend")
	public String sentMessage(Model model, @RequestParam int mno) {
	    Message message = messagedao_jpa.findById(mno).orElse(null);
        model.addAttribute("message", message);
        return "/member/messageboxsend";
	}
	
    // 마이페이지-받은 쪽지 팝업
    @GetMapping("/member/messageboxreceive")
    public String receivedMessage(Model model, @RequestParam int mno) {
        Message message = messagedao_jpa.findById(mno).orElse(null);
        if (message != null) {
            String senderId = message.getMid();
            Optional<Member> senderOptional = memberdao_jpa.findById(senderId);
            if (senderOptional.isPresent()) {
                Member sender = senderOptional.get();
                model.addAttribute("senderNickname", sender.getNickname());
            }
            model.addAttribute("message", message);
        }
        return "/member/messageboxreceive";
    }
	
	//마이페이지-쪽지 보내기
    @PostMapping("/member/sendmessage")
    @ResponseBody
    public String sendMessage(@RequestParam("nickname") String nickname, @RequestParam("content") String content, HttpSession session, RedirectAttributes redirectAttributes){
        // 세션에서 로그인된 사용자의 ID 가져오기
        String senderId = ((Member) session.getAttribute("m")).getId();
        // 발신 아이디, 닉네임을 가져와 Member 엔티티 조회
        Member sender = memberdao_jpa.findById(senderId).orElse(null);
        
        if (sender != null) {
            // 받는 사람 닉네임으로 Member 엔티티 조회
            Member receiver = memberdao_jpa.findByNickname(nickname).orElse(null);
            if (receiver == null) {
                return "닉네임이 존재하지 않습니다. 쪽지 수신자 닉네임을 확인해 주세요.";
            }
            
            // Message 엔티티 생성 및 설정
            Message message = new Message();
            message.setMid(senderId);
            message.setMcontent(content);
            message.setMember(receiver); // 수신자 설정
            message.setRegdate(new Date()); // 현재 시각 설정
            message.setDeletedBySender(false);
            message.setDeletedByReceiver(false);
            
            // mno 설정 (mno 최대값 + 1)
            int maxMno = messagedao_jpa.findMaxMno();
            message.setMno(maxMno + 1);

            // 메시지 저장
            messagedao_jpa.save(message);
            
            return "쪽지가 성공적으로 보내졌습니다.";
        } else {
            return "쪽지 보내기에 실패하였습니다.1";
        }
    }
    
	//마이페이지-쪽지쓰기 팝업
	@GetMapping("/member/messagesend")
	public void sendMessage(@RequestParam String id,Model model) {
		String nickname = "";
		if(id != null && !id.equals("")) {
			nickname = memberdao_jpa.findNicknameById(id);
		}
		model.addAttribute("nickname", nickname);
	}
	
	// 마이페이지-닉네임 중복 체크
    @GetMapping("/member/checkNickname")
    @ResponseBody
    public Map<String, Boolean> checkNickname(@RequestParam String nickname) {
        Map<String, Boolean> response = new HashMap<>();
        int count = memberdao_jpa.countByNickname(nickname);
        response.put("exists", count > 0);
        return response;
    }
	
	//마이페이지-쪽지 삭제(체크박스 통해서 삭제)
	@PostMapping("/member/deletemessages")
	@ResponseBody
	public String deleteMessages(@RequestParam("messageIds") List<Integer> messageIds, HttpSession session) {
	    Member loggedInMember = (Member) session.getAttribute("m");
	    for (int mno : messageIds) {
	        Message message = messagedao_jpa.findById(mno).orElse(null);
	        if (message != null) {
	            if (message.getMid().equals(loggedInMember.getId())) {
	                // 보낸 쪽지인 경우
	                message.setDeletedBySender(true);
	            } else {
	                // 받은 쪽지인 경우
	                message.setDeletedByReceiver(true);
	            }
	            messagedao_jpa.save(message);
	        }
	    }
	    return "쪽지가 삭제되었습니다.";
	}

	//마이페이지-쪽지 삭제(휴지통 이미지 클릭해서 1개 삭제)
	@PostMapping("/member/deletemessage")
	@ResponseBody
	public String deleteMessage(@RequestParam("mno") int mno, HttpSession session) {
	    Message message = messagedao_jpa.findById(mno).orElse(null);
	    if (message != null) {
	        // 해당 쪽지를 논리적으로 삭제 처리 (발신자 또는 수신자에 따라 쪽지를 삭제 처리)
	        if (message.getMid().equals(((Member) session.getAttribute("m")).getId())) {
	            // 로그인 사용자가 보낸 쪽지인 경우
	            message.setDeletedBySender(true);
	            
	        } else {
	            // 로그인 사용자가 받은 쪽지인 경우
	            message.setDeletedByReceiver(true);
	            
	        }
	        messagedao_jpa.save(message);
	        return "쪽지가 삭제되었습니다.";
	    } else {
	        return "해당 쪽지를 찾을 수 없습니다.";
	    }
	}
	
	//마이페이지-받은 쪽지함
	@GetMapping("/member/mypagemessage")
	public String myPageMessage(@RequestParam(name = "page", defaultValue = "0") int page, Model model, HttpSession session) {
	    String id = ((Member) session.getAttribute("m")).getId();

	    int pageSize = 25;

	    Page<Message> messagePage = messagedao_jpa.findPagedReceivedMessages(id, PageRequest.of(page, pageSize));

	    //mid 기준 닉네임 출력하기 위한 코드
	    List<Message> messages = messagePage.getContent();
	    Map<String, String> senderNicknames = new HashMap<String, String>();

	    for (Message message : messages) {
	        String senderId = message.getMid();
	        Optional<Member> senderMember = memberdao_jpa.findById(senderId);
	        String senderNickname = senderMember.map(Member::getNickname).orElse("");
	        senderNicknames.put(senderId, senderNickname);
	    }

	    model.addAttribute("messagePage", messagePage);
	    model.addAttribute("senderNicknames", senderNicknames);
	    model.addAttribute("currentPage", page);

	    return "/member/mypagemessage";
	}

    //마이페이지-보낸 쪽지함
    @GetMapping("/member/mypagemessagesend")
	public String mypagemessagesend(@RequestParam(name = "page", defaultValue = "0") int page, Model model, HttpSession session) {
	    String id = ((Member) session.getAttribute("m")).getId();
	    
	    //한 페이지에 보여줄 개수
	    int pageSize = 25;
	    
	    // 보낸 쪽지 목록을 페이지로 분할하여 가져옴 (삭제되지 않은 쪽지만 조회)
	    Page<Message> messagePage = messagedao_jpa.findPagedSentMessages(id, PageRequest.of(page, pageSize));
	    model.addAttribute("messagePage", messagePage);
	    model.addAttribute("currentPage", page);
	    
	    return "/member/mypagemessagesend";
	}
	
	/* -----------마이페이지-위시리스트------------ */

	@GetMapping("/member/mypagelike")
	public void messagelike(HttpSession session,Model model, @RequestParam(defaultValue = "1") int page) {
		// 세션에서 로그인된 사용자의 ID 가져오기
	    String id = ((Member) session.getAttribute("m")).getId();
	    //관심목록 가져오기
	    int pageSize = 8;
	    Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("eventno").descending());
	    Page<Event> eventPage;
	    eventPage=eventdao_jpa.findWishListById(id, pageable);
	    List<List<EventVO>> rows = new ArrayList<>();
	    List<EventVO> currentRow = null;

	    for (Event event : eventPage.getContent()) {
	        String state = calculateEventStatus(event);
	        EventVO eventVO = changeEventtoeventvo(event);
	        eventVO.setEventState(state);

	        if (currentRow == null || currentRow.size() >= 4) {
	            currentRow = new ArrayList<>();
	            rows.add(currentRow);
	        }
	        currentRow.add(eventVO);
	    }
	    
	    model.addAttribute("rows", rows);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", eventPage.getTotalPages());
	    
	}
	//공연중 상태 확인
    public String calculateEventStatus(Event event) {
        LocalDate currentDate = LocalDate.now(); // 현재 날짜를 가져옵니다.

        if (event.getEventstart() != null && event.getEventend() != null) {
            if (currentDate.isBefore((LocalDate)(event.getEventstart().toInstant()
            	      .atZone(ZoneId.systemDefault())
            	      .toLocalDate()))) {
                // 이벤트 시작일과 현재 날짜를 비교하여 D-day를 계산합니다.
                long daysUntilEvent = ChronoUnit.DAYS.between(currentDate, event.getEventstart().toInstant()
                	      .atZone(ZoneId.systemDefault())
                	      .toLocalDate());
                return ("D-" + daysUntilEvent);
            } else if (currentDate.isAfter(event.getEventend().toInstant().atZone(ZoneId.systemDefault())
            	      .toLocalDate())) {
            	return "공연종료";
            } else {
            	return "공연중";
            }
        } else {
            return "미정";
        }
    }
  //event 엔티티 eventvo 로 이동. 공연 진행 상태 추가해서 쓰려고
  	public EventVO changeEventtoeventvo(Event event) {
  		EventVO eventVO = new EventVO();
  		eventVO.setEventno(event.getEventno());
  		eventVO.setCategoryno(event.getCategoryno());
  		eventVO.setEventname(event.getEventname());
  		eventVO.setEventaddr(event.getEventaddr());
  		eventVO.setEventplace(event.getEventplace());
  		eventVO.setEventcontent(event.getEventcontent());
  		eventVO.setEventstart(event.getEventstart());
  		eventVO.setEventend(event.getEventend());
  		eventVO.setEventlat(event.getEventlat());
  		eventVO.setEventlong(event.getEventlong());
  		eventVO.setEventhit(event.getEventhit());
  		eventVO.setEventlink(event.getEventlink());
  		eventVO.setEventfname(event.getEventfname());
  		eventVO.setEventticket(event.getEventticket());
  		eventVO.setEventprice(event.getEventprice());
  		eventVO.setParkplace(event.getParkplace());
  		return eventVO;
  	}
	
	/* -----------마이페이지-회원정보 수정------------ */
		
	
	@GetMapping("/member/editmypage")
	public void editmypageform() {
	}
	
	@GetMapping("/member/editpic")
	public void editpicform() {
	}
	
	@PostMapping("/member/editpic")
	public String editpic(HttpSession session, MultipartFile uploadFile) {
		Member m = (Member) session.getAttribute("m");
		
		// 사진 경로 지정
		String path =  Paths.get(System.getProperty("user.dir"))
			    .resolve("src").resolve("main").resolve("resources").resolve("static").resolve("profileImage").toString();
	//	System.out.println("path: "+path);
		
		// 원래 파일 이름 받아 oldfname에 저장
		String oldfname = m.getFname();
		String newfname = null;
		
		//업로드한 파일명을 fname변수에 저장
		newfname = uploadFile.getOriginalFilename();

		// 만약 파일이 바뀌었다면
		if(newfname != null && !newfname.equals("")) {
			try {
				
				// 바뀐 파일 저장
				FileOutputStream fos = new FileOutputStream(path+"/"+newfname);
				FileCopyUtils.copy(uploadFile.getBytes(), fos);
				fos.close();
				
				// 기존 파일이 기본(profile) 사진이 아니었다면 삭제
				if(!oldfname.equals("profile.png")) {
					File file = new File(path+"/"+oldfname);
					file.delete();
				}

				
			}catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		// 파일이 바뀌지 않았다면 기존 파일 유지
		else {
			newfname = oldfname;
		}

		m.setFname(newfname);
		// 업로드 파일 없음: 기존 파일이름 
		// 있음: 새 파일이름
		memberdao_jpa.save(m);
		
		session.setAttribute("m", m);
	//	request.setAttribute("oldfname", oldfname);
		return "/member/windowclose";
	}
	
	
	// 사진 삭제 후 기본프로필 사진으로 변경
	@RequestMapping("/deletepic")
	@ResponseBody
	public void deletepic(HttpSession session) {
		
		Member m = (Member)session.getAttribute("m");
		String fname = m.getFname();
		
		// 경로 받아오기
		String path =  Paths.get(System.getProperty("user.dir"))
			    .resolve("src").resolve("main").resolve("resources").resolve("static").resolve("profileImage").toString();
	//	System.out.println("path: "+path);
		
		// 파일 삭제
		File file = new File(path+"/"+fname);
		file.delete();
		
		// 기본 프로필로 변경
		m.setFname("profile.png");
		memberdao_jpa.save(m);
		session.setAttribute("m", m);
	}


	// 이메일 인증
	@GetMapping("/vaildEmail")
	@ResponseBody
	public String validEmail(String email) {
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		
		// 인증번호 난수 생성
		Random r = new Random();
		int a = r.nextInt(10);		
		int b = r.nextInt(10);		
		int c = r.nextInt(10);
		int d = r.nextInt(10);
		int e = r.nextInt(10);
		int f = r.nextInt(10);
		String data = a+""+b+""+c+""+d+""+e+""+f;
		
		mailMessage.setFrom("green3potato@gmail.com");
		mailMessage.setTo(email);
		mailMessage.setSubject("Culturemore 이메일 인증번호 입니다."); // 제목
		mailMessage.setText("인증번호: "+data); // 내용
		
		try {
			mailSender.send(mailMessage);
		}catch (Exception ex) {
			System.out.println("예외발생: "+ex.getMessage());
		}
		return data;
	}
	
	
	// 이메일을 수정했고, 인증 받았으면 1
	@RequestMapping(value="/changeEmail")
	@ResponseBody
	public int changeEmail() {
		return 1;
	}
	
	// 마이페이지-비밀번호 변경 페이지
	@GetMapping("/member/changepwd")
	public void changepwdform() {
	}
	
	// 마이페이지-비밀번호 변경 시
	@RequestMapping(value="/member/changepwd", method = { RequestMethod.POST })
	@ResponseBody
	public Object changepwd(HttpSession session,@RequestParam("pwd")  String pwd,@RequestParam("newpwd") String newpwd,
			@RequestParam("chkpwd") String chkpwd) {
		Member m = (Member) session.getAttribute("m");
		
		// 회원 기존 비밀번호 받음
		String userpwd = m.getPwd();
		
		// alert문 담을 msg 생성
		String msg = "";
		if(pwd.equals(userpwd)) { // 현재 비밀번호 일치
			
			if(newpwd.equals(chkpwd)) { // 새 비밀번호와 새 비밀번호 확인이 일치하면 변경
				msg = "비밀번호 변경이 완료되었습니다.";
				m.setPwd(newpwd);
				memberdao_jpa.save(m);
			}
			else { // 새 비밀번호와 새 비밀번호 확인이 다르면 바뀌지 않음
				msg = "새 비밀번호가 일치하지 않습니다.";
			}
		}
		
		// 현재 비밀번호가 일치하지 않으면 
		else {
			msg = "현재 비밀번호와 일치하지 않습니다.";
		}
		return msg;
	}
	
	
	@RequestMapping("/chknickname")
	@ResponseBody
	public int chknickname(String nickname) {
		
		int re = 1; // 없는 경우 1, 존재하는 경우 2
		int cnt = memberdao_jpa.countByNickname(nickname);
		if(cnt != 0) { // 존재함
			re = 2;
		}
		return re;
	}
	
	// 마이페이지 닉네임과 이메일 변경 시
	@PostMapping("/member/editmypage")
	public ModelAndView editmypage(HttpSession session, String nickname, String email, int chkemail, int chknickname) {
		ModelAndView mav = new ModelAndView();
		
		Member m = (Member)session.getAttribute("m");
		
		// 닉네임이 바뀌었다면 수정
		if(nickname != null && !nickname.equals("")) {
			if(!nickname.equals(m.getNickname())) {
				if(chknickname == 1) {
					m.setNickname(nickname);
				}
			}
			// 바뀐 값이 전과 같음
			else {
				chknickname = 1;
			}
		}
		
		// 이메일 수정
		if(email != null && !email.equals("")) {
			// 바뀐 값이 전과 다름
			if(!email.equals(m.getEmail())) {
				// 이메일 인증을 완료 함
				if(chkemail == 1) {
					m.setEmail(email);
				}
			}
			// 바뀐 값이 전과 같음
			else {
				chkemail = 1;
			}
		}
		
		
		if(chknickname == 1 && chkemail == 1) {
			mav.addObject("msg", "회원정보 수정이 완료되었습니다.");
			mav.addObject("pagehref", "/member/mypage");
		}
		else if(chknickname != 1){
			mav.addObject("nicknamemsg", "닉네임 중복확인이 되지 않았습니다.");
			if(chkemail != 1) {
				mav.addObject("emailmsg", "이메일 인증이 되지 않았습니다.");
			}
			mav.addObject("pagehref", "/member/editmypage");
		}
		else {
			if(chkemail != 1) {
				mav.addObject("emailmsg", "이메일 인증이 되지 않았습니다.");
			}
			mav.addObject("pagehref", "/member/editmypage");
		}
		mav.setViewName("/member/editmypage");
		memberdao_jpa.save(m);
		return mav;
	}
	
	/* -----------마이페이지-회원탈퇴------------ */
	
	// 마이페이지-회원탈퇴
	@GetMapping("/member/deletemember")
	public ModelAndView deletemember(HttpSession session) {
		ModelAndView mav = new ModelAndView("redirect:/mainPage");
		String id = ((Member) session.getAttribute("m")).getId();
		
		// 회원탈퇴
		memberdao_jpa.deleteById(id);
		session.setAttribute("m", null); // 세션파기
		return mav;
	}
	
}
