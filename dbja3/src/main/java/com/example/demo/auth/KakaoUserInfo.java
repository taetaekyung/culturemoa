package com.example.demo.auth;


import java.util.Map;

import com.example.demo.entity.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class KakaoUserInfo {
	private final Map<String, Object> attributes; // OAuth2 반환하는 유저 정보 Map
    private final String nameAttributeKey;
    private final String name;
    private final String email;
    private final String gender;
    private final String birthday;
    private final String where;

    public static KakaoUserInfo of(String registrationId, Map<String, Object> attributes) {
        return ofKakao(registrationId, "id", attributes);
    }


    public static KakaoUserInfo ofKakao(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
    	// kakao는 kakao_account에 유저정보가 있다. (email)
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
        // kakao_account안에 또 profile이라는 JSON객체가 있다. (nickname, profile_image)
        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");

    	return KakaoUserInfo.builder()
    			.attributes(attributes)
    			.name((String) kakaoProfile.get("nickname"))
    			.nameAttributeKey(userNameAttributeName)
                .email((String) kakaoAccount.get("email"))
                .gender((String) kakaoAccount.get("gender"))
                .birthday((String) kakaoAccount.get("birthday"))
                .where(registrationId)
                .build();
    }

    public Member toEntity(){
    	return Member.builderKakao().id(name)
    			.email(email).gender(gender)
    			.birth(birthday).where(where)
    			.build();
    }
}