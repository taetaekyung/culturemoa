package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final UserService userService;
	
	@Bean
	public SecurityFilterChain filerChain(HttpSecurity http) throws Exception {
        
		// http.csrf().disable();
		
		//security가 제공해주는 로그인 폼 말고 우리가 만들거야
		http.authorizeHttpRequests() //내가 원하는 인증과 인가에 대한 설정을 한다
		// 정적파일 permit
		.requestMatchers("/error","/styles/**",  "/assets/**","/images/**","/profileImage/**","/summernote/**").permitAll()
		.requestMatchers("", "/", "/mainPage", "/calendarEvent", "/calendarTicket", "selectArea", "main_insertTalk", 
				"main_updateTalk", "/searchresult" // 여기까지 mainPage 관련 mapping 
				, "/layout/default_layout", // 여기까지 layout 관련 mapping 
				 "/sign", "/login/findid", "/login/findidcomplete", "/checkInfo", "/login/findpassword"
				, "/login/passwordreset", "/changePwd", "/login/passwordresetcomplete" // 여기까지 login 관련 mapping 
				, "/signup",  "/checkId","/vaildEmail",  "/checkNickname" // 여기까지 회원가입 관련 mapping
				, "/event/domesticconcertlist", "/event/festivallist", "/event/koreaconcertlist", "/event/musicallist", "/event/playlist"
				, "/event/eventdetail"  // 여기까지 event 관련 mapping
				, "/boards/board/freelist/**",  "/boards/board/togetherlist/**", "/boards/review/reviewlist/**" 
				, "/updateHit", "/boards/review/reviewDetail", "/updatefreeHit", "/boards/board/freeDetail" // 여기까지 board 관련 mapping
				, "/teaminfo", "/pageinfo" // 여기까지 사이트소개 관련 mapping
				, "/opentalk", "/member/sendmessage", "updateTalk" // 여기까지 오픈톡 관련 mapping
				, "/FAQ/**").permitAll() //이 요청은 모두 사용가능
		.anyRequest().authenticated(); //나머지는 인증 필요
		
		// form post 방식이지만 login이 필요없는 searchresult의 경우, csrf 무시함.
		http.csrf().ignoringRequestMatchers("/uploadSummernoteImageFile","boardUpdate", "togetherUpdate", "freeUpdate", "/sign", "/searchresult","/member/editmypage");http.csrf().ignoringRequestMatchers("/sign", "/searchresult");
		
		http.formLogin().loginPage("/login/login").permitAll() //로그인은 여기서해
		.defaultSuccessUrl("/mainPageAfterLogin"); //성공하면 main으로 갈게
	
		
		http.logout() //패턴 링크만 적어주면 알아서 로그아웃을 해줄거야
		.logoutRequestMatcher(new AntPathRequestMatcher("/login/logout")) //패턴이름 logout
		.invalidateHttpSession(true)	//로그아웃하면 session을 파기해
		.logoutSuccessUrl("/mainPage") //로그아웃 성공하면 이리로가
		 .and()
		 .oauth2Login()
		 .loginPage("/login/login")
		 .defaultSuccessUrl("/mainPage")
		 .failureUrl("/login/login")
		 .userInfoEndpoint()
		 .userService(userService);
		
		http.httpBasic(); //나머지는 기본 설정에 따라라
		
		return http.build();
	}
	
}
