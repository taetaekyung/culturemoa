package com.example.demo.controller;


import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dao.MemberDAO_jpa;
import com.example.demo.dao.MessageDAO_jpa;
import com.example.demo.entity.Member;
import com.example.demo.entity.Message;

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
	
	
	/* -----------마이페이지-회원정보------------ */
	
	
	@GetMapping("/member/mypage")
	public void mypage(HttpSession session) {
	}
	

	/* -----------마이페이지-쪽지함------------ */
	
	//마이페이지-받은 쪽지함
    @GetMapping("/member/mypagemessage")
    public String myPageMessage(Model model, HttpSession session) {
    	String id = ((Member)session.getAttribute("m")).getId();
    	List<Message> message = messagedao_jpa.findByReceiveId(id);
    	model.addAttribute("messageList",message);
    	return "/member/mypagemessage";
    }
    
	//마이페이지-보낸 쪽지함
	@GetMapping("/member/mypagemessagesend")
	public String messgagesend(Model model, HttpSession session) {
		String id = ((Member)session.getAttribute("m")).getId();
		List<Message> message = messagedao_jpa.findBySendId(id);
		model.addAttribute("messageList",message);
		return "/member/mypagemessagesend";
	}
	
	/* -----------마이페이지-위시리스트------------ */

	@GetMapping("/member/mypagelike")
	public void messagelike() {
	}
	
	
	/* -----------마이페이지-회원정보 수정------------ */
		
	
	@GetMapping("/member/editmypage")
	public void editmypageform(Model model, Member m) {
		model.addAttribute("m", m);
	}
	
	@GetMapping("/member/editpic")
	public void editpic(HttpSession session, HttpServletRequest request) {
		Member m = (Member) session.getAttribute("m");
		String path =  Paths.get(System.getProperty("user.dir"))
			    .resolve("src").resolve("main").resolve("resources").resolve("static").resolve("profileImage").toString();
		System.out.println("path: "+path);
	/*	MultipartFile uploadFile = m.getFname();
		
		m.setFname("");
		String fname = null;
		
		//업로드한 파일명을 fname변수에 저장
		fname = uploadFile.getOriginalFilename();

		if(!fname.equals("") && fname != null) { //업로드한 파일이 있다면
			System.out.println("업로드 파일이 있어요");
			try {
				byte []data = uploadFile.getBytes(); //파일의 내용을 바이트로 가져옴
				fname = uploadFile.getOriginalFilename(); //업로드한 파일 이름 가져오기
				FileOutputStream fos = new FileOutputStream(path+"/"+fname); //파일 출력을 위한 스트림 생성
				fos.write(data); //파일에 출력
				fos.close(); 

			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		else {
			System.out.println("업로드 파일이 없어요");
		}
		
		g.setFname(fname);// 업로드 파일 없음: "" 있음: 파일이름

		dao.insert(g); */
	}
	

	// 이메일 인증
	@GetMapping("/vaildEmail")
	@ResponseBody
	public String validEmail(String email) {
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		
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
		String userpwd = m.getPwd();
		String msg = "";
		if(pwd.equals(userpwd)) {
			if(newpwd.equals(chkpwd)) {
				msg = "비밀번호 변경이 완료되었습니다.";
				m.setPwd(newpwd);
				memberdao_jpa.save(m);
			}
			else {
				msg = "새 비밀번호가 일치하지 않습니다.";
			}
		}
		else {
			msg = "현재 비밀번호와 일치하지 않습니다.";
		}
		return msg;
	}
	
	
	// 마이페이지 닉네임과 이메일 변경 시
	@PostMapping("/member/editmypage")
	public ModelAndView editmypage(HttpSession session, String nickname, String email, int chkemail) {
		ModelAndView mav = new ModelAndView();
		
		Member m = (Member)session.getAttribute("m");
		
		if(nickname != null && !nickname.equals("")) {
			m.setNickname(nickname);
		}
		System.out.println(chkemail);
		if(email != null && !email.equals("")) {
			if(!email.equals(m.getEmail())) {
				if(chkemail == 1) {
					m.setEmail(email);
					mav.addObject("msg", "회원정보 수정이 완료되었습니다.");
					mav.addObject("pagehref", "/member/mypage");
				}
				else {
					mav.addObject("msg", "이메일 인증이 되지 않았습니다.");
					mav.addObject("pagehref", "/member/editmypage");
				}
			}
			else {
				mav.addObject("msg", "회원정보 수정이 완료되었습니다.");
				mav.addObject("pagehref", "/member/mypage");
			}
		}
		mav.setViewName("/member/editmypage");
		memberdao_jpa.save(m);
		return mav;
	}
	
	/* -----------마이페이지-회원탈퇴------------ */
	
	// 마이페이지-비밀번호 변경 페이지
	@GetMapping("/member/deletemember")
	public ModelAndView deletemember(HttpSession session) {
		ModelAndView mav = new ModelAndView("redirect:/mainPage");
		String id = ((Member) session.getAttribute("m")).getId();
		memberdao_jpa.deleteById(id);
		session.setAttribute("m", null); // 세션파기
		return mav;
	}
	
}
