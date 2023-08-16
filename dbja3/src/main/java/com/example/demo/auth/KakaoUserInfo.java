package com.example.demo.auth;


import java.util.Map;

import com.example.demo.entity.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KakaoUserInfo {
	private Map<String, Object> attributes; // OAuth2 반환하는 유저 정보 Map
    private String nameAttributeKey;
    private String name;
    private String email;
    private String gender;
    private String birthday;
    private String kakao;

    @Builder
    public KakaoUserInfo(Map<String, Object> attributes, String kakao, String nameAttributeKey, String name, String email, String gender, String birthday) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.birthday = birthday;
        this.kakao = kakao;
    }

    public static KakaoUserInfo of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
    	// kakao는 kakao_account에 유저정보가 있다. (email)
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
        // kakao_account안에 또 profile이라는 JSON객체가 있다. (nickname, profile_image)
        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");

    	return KakaoUserInfo.builder()
    			.name((String) kakaoProfile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .gender((String) kakaoAccount.get("gender"))
                .birthday((String) kakaoAccount.get("birthday"))
                .kakao(registrationId)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Member toEntity(){
    	return Member.builder().id(name)
    			.email(email).gender(gender)
    			.birth(birthday).kakao(kakao)
    			.build();
    }
}