package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@NoArgsConstructor
@Getter
@Table(name="member")
public class Member {
	@Id
	private String id;
	
	private String pwd;
	private String name;
	private String birth;
	private String email;
	private String gender;  //'남성', '여성'
	private String role;    //'admin','user'
	private String phone; // 숫자만
	private String nickname; // 6자 이하
	private String fname; // id.jpg
	
	@Transient
	private String kakao;
	
	
	@OneToMany(mappedBy = "member", cascade=CascadeType.REMOVE)
	private List<Message> messages = new ArrayList<>();
	
	@OneToMany(mappedBy = "member", cascade=CascadeType.REMOVE)
	private List<Comments> comments = new ArrayList<>();
	
	@OneToMany(mappedBy = "member", cascade=CascadeType.REMOVE)
	private List<Reviewboard> reviewBoards = new ArrayList<>();
	
	@OneToMany(mappedBy = "member", cascade=CascadeType.REMOVE)
	private List<Reviewcomment> reviewComments = new ArrayList<>();
	
	@OneToMany(mappedBy = "member", cascade=CascadeType.REMOVE)
	private List<Wishlist> wishLists = new ArrayList<>();
	
	@Builder
    public Member(String id, String email, String gender, String birth, String kakao) {
        String userid = email.substring(0, email.indexOf("@"));
		this.id = userid;
        this.pwd = "dbja2023";
        this.email = email;
        this.name = id;
        this.nickname = id;
        this.phone = "01000000000";
        this.fname = "profile.png";
        this.role = "user";
        if(gender.equals("female")) {
        	this.gender = "여성";
        }
        else {
        	this.gender = "남성";
        }
        birth = birth.substring(0, 2)+"-"+birth.substring(2, 4);
        this.birth = birth;
        this.kakao = kakao;
    }
	
}
