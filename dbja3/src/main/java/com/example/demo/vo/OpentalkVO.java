package com.example.demo.vo;

import java.sql.Date;

import lombok.Data;


@Data
public class OpentalkVO {
	private int talkno;
	private String talkcontent;
	private String id; 
	private Date regdate;
	
	//member와의 mapping을 위해 member.gender
	private String gender;
}
