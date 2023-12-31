package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dao.MemberDAO_jpa;
import com.example.demo.entity.Member;

import jakarta.servlet.http.HttpSession;
import lombok.Setter;

@Controller
@Setter
public class LoginController {

	@Autowired
	private MemberDAO_jpa memberdao_jpa;
	
	//로그인 화면
	@GetMapping("/login/login")
	public void loginform() {
		
	}
	
	//로그인 화면
	@GetMapping("/mainPageAfterLogin")
	public ModelAndView login(HttpSession session, String id, String password) {
		
		// 카카오 로그인이 아닌 경우는 아직 session으로 값을 넘겨주지 않음
		// 카카오 로그인의 경우 로그인하면서 따로 넘겨줌.
		if(session.getAttribute("m") == null) {
		// 로그인된 회원의 정보를 가져오기 위하여 
		// 시큐리티의 인증 객체 필요
		  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		  // 위의 인증 객체를 통해 로그인된 user 객체를 받아옴
		  User user = (User) authentication.getPrincipal();
		  
		  // user를 통해 로그인한 회원의 id 가져옴
		  String userid = user.getUsername();
		  Member m = memberdao_jpa.findByUserId(userid);
		  session.setAttribute("m", m);
		  System.out.println(m.getWhere());
		}
		ModelAndView mav = new ModelAndView("redirect:/mainPage");

/*
		if(memberdao_jpa.findByUserId(id) == null || memberdao_jpa.findByUserId(id).equals("")) {
			mav.addObject("msg", "존재하지 않는 아이디입니다.");
			mav.setViewName("/login/login");
		}
		else {
			String pwd = ((Member)memberdao_jpa.findById(id).get()).getPwd();
			if(!pwd.equals(password)) {
				mav.addObject("msg", "비밀번호가 일치하지 않습니다.");
				mav.setViewName("/login/login");
			}
			else {
				System.out.println("세션 넘겨주러 왔음.");
				session.setAttribute("m", (Member)memberdao_jpa.findById(id).get());
			}
		} */
		return mav;
	}
	
	//로그아웃 기능
	@GetMapping("/login/logout")
	public String logout(HttpSession session) {
		session.invalidate(); // 로그아웃 시 세션파기
		return "redirect:/mainPage";
	}
	
	//아이디 찾기
	@GetMapping("/login/findid")
	public void findid() {
		
	}
	//아이디 찾기 결과
	@GetMapping("/login/findidcomplete")
	public void findidcomplete(@RequestParam("name") String name, @RequestParam("email") String email,Model model) {
		String findId= memberdao_jpa.findIdByNameAndEmail(name, email);
		model.addAttribute("findId",findId);
		
	}
	//비밀번호 찾기 ajax
	@GetMapping("/checkInfo")
	@ResponseBody
	public String checkInfo(String name,String id,String email) {
		String pwd="";
		pwd=memberdao_jpa.findPwdByNameAndEmailAndId(name, email, id);
		if(pwd==null) {
			id="";
		}
		return id;
	}
	
	//비밀번호 찾기
	@GetMapping("/login/findpassword")
	public void findpassword() {
		
	}
	//비밀번호 재설정
	@GetMapping("/login/passwordreset")
	public void passwordreset(Model model,@RequestParam("id")String id) {
		model.addAttribute("id", id);
	}
	
	//비밀번호 재설정하기
	@GetMapping("/changePwd")
	@ResponseBody
	public void changePwd(String id,String newPwd) {
		memberdao_jpa.updatePwdById(id, newPwd);
	}
	
	//비밀번호 재설정 결과
	@GetMapping("/login/passwordresetcomplete")
	public void passwordresetcomplete() {
		
	}
	
}
