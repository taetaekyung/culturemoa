package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.dao.MemberDAO_jpa;
import com.example.demo.entity.Member;

import lombok.Setter;

@Service
@Setter
public class MemberService implements UserDetailsService {
	
	@Autowired
	private MemberDAO_jpa memberdao;

	//스프링시큐리티에서는 로그인을 위한 get 방식 폼태그는 우리 마음대로 만듦
	// 실제 로그인 처리는 우리가 안하고 시큐리티가 알아서 해줌
	// 이 때 다음의 loadUser어쩌고 메소드 동작
	// 사용자의 입력된 아이디 전달됨.
	// UserDetails 객체를 생성하여 반환하도록 만들어야 함.
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails user=null;
		// 비밀번호 암호화
		Member m = memberdao.findById(username).get();
		if(m == null) {
			try {
				throw new UsernameNotFoundException(username);				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			user = User.builder().username(username)
					.password(m.getPwd()).roles(m.getRole()).build();
		}
		
		System.out.println(user.getPassword());
		return user;
	}

}
