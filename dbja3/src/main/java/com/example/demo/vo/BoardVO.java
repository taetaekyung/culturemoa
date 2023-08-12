package com.example.demo.vo;

import java.util.Date;

import lombok.Data;

@Data
public class BoardVO {
	private int boardno;
	private String bcategory;
	private String boardtitle;
	private String boardcontent;
	private Date regdate;
	private String id;
	private String nickname;
	private int boardhit;
	private int boardlikes;
}
