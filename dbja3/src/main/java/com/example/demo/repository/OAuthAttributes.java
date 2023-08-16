package com.example.demo.repository;

import java.util.Map;


import com.example.demo.dao.MemberDAO_jpa;
import com.example.demo.entity.Member;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class OAuthAttributes {
	private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String name;
    private final String email;
    private final String picture;
    private final String gender;
    private final String nickname;
    private final String mobile;
    
    public static OAuthAttributes of(String registrationId,String userNameAttributeName, Map<String, Object> attributes) {
        if("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }
        else {
          //  return ofKakao("id", attributes);
        	return null;
        }
    }
    
    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
       
        
        return OAuthAttributes.builder()
                .nickname((String) response.get("nickname"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .gender((String) response.get("gender"))
                .name((String) response.get("name"))
                .mobile((String) response.get("mobile"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
    public Member toMember() {
    	 Member member=new Member();
         member.setId(nickname);
         member.setEmail(email);
         if(gender.equals("F")) {
         	member.setGender("여성");
         }else {
         	member.setGender("남성");
         }
         member.setPwd(nickname);
         member.setPhone(mobile);
         member.setName(name);
         member.setNickname(nickname);
         member.setRole("user");
         member.setFname("profile.png");
         return member;
    }
    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .gender(gender)
                .nickname(nickname)
                .mobile(mobile)
                .role(Role.GUEST)
                .build();
    }
  
}
