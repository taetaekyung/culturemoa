package com.example.demo.entity;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class SessionUser implements Serializable {
	//인증된 사용자 정보들만 담을 수 있는 클래스이다.
	//엔티티를 주고 받을 때 직접적으로 사용하는 것 보다 이렇게 Dto 클래스를 만들어 사용하는 것이 성능상 이점이 있다.
	SessionUser() {}

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
    }

    private String nickname;

    private String email;

    private String picture;
    
    private String gender;
    
    private String name;
    
    private String mobile;
}
