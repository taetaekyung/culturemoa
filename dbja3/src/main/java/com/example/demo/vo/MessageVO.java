package com.example.demo.vo;

import java.util.Date;

import lombok.Data;

@Data
public class MessageVO {
	private int mno;
	private String mid; // 발신아이디
	private String mcontent; 
	private String id; // 수신아이디
	private String nickname; // 수신닉네임
	private Date regdate;
}
