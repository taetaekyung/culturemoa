package com.example.demo.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dao.MemberDAO_jpa;
import com.example.demo.dao.MessageDAO_jpa;
import com.example.demo.entity.Member;
import com.example.demo.entity.Message;

import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {
	
	@Autowired
	private MemberDAO_jpa memberdao_jpa;
	
	@Autowired
	private MessageDAO_jpa messagedao_jpa;
	
	@GetMapping("/member/mypage")
	public void mypage(HttpSession session) {
	}
	
	@GetMapping("/member/editmypage")
	public void editmypageform(Model model, Member m) {
		model.addAttribute("m", m);
	}
	
	@GetMapping("/member/editpic")
	public void editpic(HttpSession session) {
	}
	
	
	@GetMapping("/member/changepwd")
	public void changepwdform() {
	}
	
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

	@GetMapping("/member/mypagelike")
	public void messagelike() {
	}

	@PostMapping("/member/editmypage")
	public ModelAndView editmypage(HttpSession session, String nickname, String email) {
		ModelAndView mav = new ModelAndView("redirect:/member/mypage");
		Member m = (Member)session.getAttribute("m");
		if(nickname != null && !nickname.equals("")) {
			m.setNickname(nickname);
		}
		if(email != null && !email.equals("")) {
			m.setEmail(email);
		}
		memberdao_jpa.save(m);
		return mav;
	}
}
