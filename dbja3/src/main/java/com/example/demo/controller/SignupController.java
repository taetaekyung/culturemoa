package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dao.MemberDAO_jpa;
import com.example.demo.entity.Member;

@Controller
public class SignupController {

	@Autowired
	private MemberDAO_jpa memberdao_jpa;
	
	@GetMapping("/signup")
	public void signup() {
	}
	
	//회원가입
	@PostMapping("/sign")
	public ModelAndView sign(Member m) {
		System.out.println(m);
		ModelAndView mav=new ModelAndView("redirect:/login/login");
		m.setRole("user");
		//회원가입
		memberdao_jpa.save(m);
		return mav;
	}
	//아이디 중복확인
	@GetMapping("/checkId")
	@ResponseBody
	public int checkId(String id){
		int check=0; //0이면 사용가능, 1이면 사용 불가능
		if(memberdao_jpa.findId(id)==null) {
			check=1;
		}
		
		return check;
	}
	
	//닉네임 중복확인
	@GetMapping("/checkNickname")
	@ResponseBody
	public int checkNickname(String nickname) {
		int check=0;
		if(memberdao_jpa.findNickname(nickname)==null) {
			check=1;
		}
		return check;
	}
}