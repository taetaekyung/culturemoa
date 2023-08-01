package com.example.demo.vo;

import java.util.Date;

import lombok.Data;

@Data
public class ReviewboardVO {
	private int reviewno;
	
	private String reviewtitle;
	private String reivewcontent;
	private int reviewhit;
	private String id;
	private String reviewfname;
	private int eventno;
	private Date regdate;
}
