package com.example.demo.repository;

import java.util.Map;

import com.example.demo.auth.KakaoUserInfo;
import com.example.demo.entity.Member;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class OAuthAttributes {
	private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String name;
    private final String email;
    private final String gender;
    private final String nickname;
    private final String mobile;
    private final String birthyear;
    private final String birthday;
    private final String where;
    

    public static OAuthAttributes ofNaver(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        
        return OAuthAttributes.builder()
        		.attributes(attributes)
                .nickname((String) response.get("nickname"))
                .email((String) response.get("email"))
                .gender((String) response.get("gender"))
                .name((String) response.get("name"))
                .mobile((String) response.get("mobile"))
                .birthyear((String) response.get("birthyear"))
                .birthday((String) response.get("birthday"))
                .where(registrationId)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
    public Member toEntity() {
    	Member m = new Member();
        System.out.println("네이버로옴");
		String userid = email.substring(0, email.indexOf("@"))+"_naver";
		m.setId(userid);
        m.setPwd("Ndbja2023");
        m.setEmail(email);
        if(name != null) {
        	m.setName(name);
        }
        else {
        	m.setName(userid);
        }
        m.setNickname(nickname);
        m.setPhone(mobile);
        m.setFname("profile.png");
        m.setRole("user");
        if(gender.equals("F")) {
        	m.setGender("여성");
        }
        else {
        	m.setGender("남성");
        }
        m.setBirth(birthyear+"-"+birthday);
        m.setWhere(where);
        
        return m;
    }

}
