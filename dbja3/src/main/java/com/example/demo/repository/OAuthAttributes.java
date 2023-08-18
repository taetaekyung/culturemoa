package com.example.demo.repository;

import java.util.Map;
import java.util.Random;

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
        
        String newnickname = nickname;
        
        if(name.equals(nickname)) {
        	Random r = new Random();
        	int a = r.nextInt(10);
        	int b = r.nextInt(10);
        	int c = r.nextInt(10);
        	int d = r.nextInt(10);
        	newnickname += a+""+b+""+c+""+d+"";
        }
        m.setNickname(newnickname);
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
    	Member m = new Member();
        m.setId(email.substring(0, email.indexOf("@")) + "_google");
        m.setPwd("Gdbja2023");
        m.setEmail(email);
        m.setName(name);
        m.setBirth("해당 정보가 없습니다.");
        
        String newnickname = name;

    	Random r = new Random();
    	int a = r.nextInt(10);
    	int b = r.nextInt(10);
    	int c = r.nextInt(10);
    	int d = r.nextInt(10);
    	newnickname += a+""+b+""+c+""+d+"";
        m.setNickname(newnickname);

        m.setFname("profile.png");
        m.setPhone("01000000000");
        m.setRole("user");
        m.setGender("해당 정보가 없습니다.");
        m.setWhere(where);
    	return m;

    }
}
