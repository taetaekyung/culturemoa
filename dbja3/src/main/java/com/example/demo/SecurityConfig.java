package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	public SecurityFilterChain filerChain(HttpSecurity http) throws Exception {
		http
        .csrf().disable();
		//security가 제공해주는 로그인 폼 말고 우리가 만들거야
		http.authorizeHttpRequests() //내가 원하는 인증과 인가에 대한 설정을 한다
	//	.requestMatchers("/","/member/login","/join").permitAll() //이 요청은 모두 사용가능
		.requestMatchers("/**").permitAll() //모두 사용가능
		.anyRequest().authenticated(); //나머지는 인증만 받으면 되
		
		http.formLogin().loginPage("/member/login").permitAll() //로그인은 여기서해
		.defaultSuccessUrl("/main"); //성공하면 main으로 갈게
		
		http.logout() //패턴 링크만 적어주면 알아서 로그아웃을 해줄거야
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) //패턴이름 logout
		.invalidateHttpSession(true)	//로그아웃하면 session을 파기해
		.logoutSuccessUrl("/member/login"); //로그아웃 성공하면 이리로가
		
		http.httpBasic(); //나머지는 기본 설정에 따라라
		return http.build();
	}
}
