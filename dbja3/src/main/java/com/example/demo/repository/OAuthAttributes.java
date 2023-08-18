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
    
    public static OAuthAttributes ofGoogle(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .attributes(attributes)
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .where(registrationId)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
    
    public Member toEntity() {
    	Member m = new Member();
        System.out.println("네이버로옴OAuthAttributes.java");
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
    public Member toEntity1() {
    	System.out.println("구글로옴OAuthAttributes.java");
        return Member.builder()
                .id(email.substring(0, email.indexOf("@")) + "_" + where)
                .email(email)
                .name(name)
                .nickname(name) // 이름을 닉네임으로 설정 (수정 가능)
                .fname("profile.png")
                .role("user")
                .gender(gender != null ? gender : "알 수 없음") // gender가 null일 때 기본값 설정
                .build();
    }
}
