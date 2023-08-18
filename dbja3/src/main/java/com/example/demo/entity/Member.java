package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
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
	private String where; // kakao, naver, google
	
	
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
	
	
	//카카오
	@Builder(builderMethodName = "builderKakao")
    public Member(String id, String email, String gender, String birth, String where) {
        System.out.println("카카오로옴Member.java");
		String userid = email.substring(0, email.indexOf("@"))+"_kakao";
		this.id = userid;
        this.pwd = "Kdbja2023";
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
        if(birth != null) {
            birth = birth.substring(0, 2)+"-"+birth.substring(2, 4);
        }
        else {
        	birth = "해당 정보가 없습니다.";
        }
        this.birth = birth;
        this.where = where;
    }
	
	//네이버
	@Builder(builderMethodName = "builderNaver")
    public Member(String id, String name, String email, String gender, String nickname, String mobile, String birthyear, String birthday,String where) {
        System.out.println("네이버로옴Member.java");
		String userid = email.substring(0, email.indexOf("@"))+"_naver";
		this.id = userid;
        this.pwd = "Ndbja2023";
        this.email = email;
        if(name != null) {
        	this.name = name;
        }
        else {
        	this.name = userid;
        }
        this.nickname = nickname;
        this.phone = mobile;
        this.fname = "profile.png";
        this.role = "user";
        if(gender.equals("F")) {
        	this.gender = "여성";
        }
        else {
        	this.gender = "남성";
        }
        this.birth = birthyear+"-"+birthday;
        this.where = where;
    }
	
}
