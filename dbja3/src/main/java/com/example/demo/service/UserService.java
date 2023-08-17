package com.example.demo.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.demo.auth.KakaoUserInfo;
import com.example.demo.dao.MemberDAO_jpa;
import com.example.demo.entity.Member;
import com.example.demo.repository.OAuthAttributes;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>  {
	@Autowired
	private MemberDAO_jpa memberdao_jpa;
	
    private final HttpSession httpSession;
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        
        Member m = new Member();
        
        if(registrationId.equals("kakao")) {
        	// OAuth2 로그인 진행 시 키가 되는 필드 값(PK) // id
            String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
            // OAuth2UserService
            KakaoUserInfo attributes = KakaoUserInfo.ofKakao(registrationId, userNameAttributeName, oAuth2User.getAttributes());
            if(memberdao_jpa.countByNameAndEmail(attributes.getWhere(), attributes.getName(), attributes.getEmail()) == 0) {
            	m = saveOrUpdateKakao(attributes);
                httpSession.setAttribute("m", m); 
            }
            
            else {
            	m = memberdao_jpa.findByNameAndEmail(attributes.getWhere(), attributes.getName(), attributes.getEmail());
            	httpSession.setAttribute("m", m);
            }
            return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(m.getRole())),
                    attributes.getAttributes(),
                    attributes.getNameAttributeKey());
        }
        
        else /*(registrationId.equals("naver"))*/ 
        {
        	String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                    .getUserInfoEndpoint().getUserNameAttributeName();
    		// naver, kakao 로그인 구분
            OAuthAttributes attributes = OAuthAttributes.ofNaver(registrationId, userNameAttributeName, oAuth2User.getAttributes());
            
            if(memberdao_jpa.countByNameAndEmail(attributes.getWhere(), attributes.getName(), attributes.getEmail())  == 0) {
            	m = saveOrUpdateNaver(attributes);
                httpSession.setAttribute("m", m); 
            }
            
            else {
            	m = memberdao_jpa.findByNameAndEmail(attributes.getWhere(), attributes.getName(), attributes.getEmail());
            	httpSession.setAttribute("m", m);
            }

            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority(m.getRole())),
                    attributes.getAttributes(),
                    attributes.getNameAttributeKey());
        }
        
    }
 // 유저 생성 및 수정 서비스 로직 (네이버)
    private Member saveOrUpdateNaver(OAuthAttributes attributes) {
    	Member m = attributes.toEntity();
    	memberdao_jpa.save(m);
        return m;
    }
    
 // 유저 생성 및 수정 서비스 로직 (카카오)
    private Member saveOrUpdateKakao(KakaoUserInfo attributes){
        Member m = attributes.toEntity();
        memberdao_jpa.save(m);
        return m;
    }
}
