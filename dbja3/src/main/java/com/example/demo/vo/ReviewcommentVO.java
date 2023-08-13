package com.example.demo.vo;


import java.util.Date;

import lombok.Data;

@Data
public class ReviewcommentVO {
	private int rcomno;
	private String rcomcontent;
	private Date regdate;
	private String id;
	private String nickname;
	private int reviewno;
}
