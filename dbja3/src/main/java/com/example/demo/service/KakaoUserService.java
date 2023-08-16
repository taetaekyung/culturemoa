package com.example.demo.service;

import com.example.demo.auth.KakaoUserInfo;
import com.example.demo.dao.MemberDAO_jpa;
import com.example.demo.entity.Member;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class KakaoUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    
	private final MemberDAO_jpa memberdao_jpa;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // OAuth2 서비스 id (구글, 카카오, 네이버)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // OAuth2 로그인 진행 시 키가 되는 필드 값(PK) // id
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        // OAuth2UserService
        KakaoUserInfo attributes = KakaoUserInfo.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Member m = saveOrUpdate(attributes);
        httpSession.setAttribute("m", m); 

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(m.getRole())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    // 유저 생성 및 수정 서비스 로직
    private Member saveOrUpdate(KakaoUserInfo attributes){
        Member m = attributes.toEntity();
        memberdao_jpa.save(m);
        return m;
    }
}