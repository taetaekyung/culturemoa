package com.example.demo.vo;

import java.util.Date;

import lombok.Data;

@Data
public class BoardVO {
	private int no;
	private String bcategory;
	private String title;
	private Date regdate;
	private String id;
	private int hit;
	private int likes;
}
