package com.example.demo.vo;

import lombok.Data;

@Data
public class EventVO {
	private int eventno; // 행사번호
	private String eventname; // 행사이름
	private String eventaddr; // 행사도로명주소
	private String eventstart; // 행사시작일
	private String eventend; // 행사종료일
	private int eventhit; // 행사조회수
	private String eventfname; // 행사사진
	private String parkplace; // 주차장여부
	private String eventState; // 행사진행상태
	private String rnum; //rownum
}
