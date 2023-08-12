package com.example.demo.vo;

import java.util.Date;

import lombok.Data;

@Data
public class CommentsVO {
	private int comno;
	private String comcontent;
	private String id;
	private String nickname;
	private int boardno;
	private Date regdate;
}
