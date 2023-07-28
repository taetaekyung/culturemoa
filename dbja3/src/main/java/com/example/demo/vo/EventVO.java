package com.example.demo.vo;

import java.util.Date;

import lombok.Data;

@Data
public class EventVO {
	private int eventno; // 행사번호
	private int categoryno; // 행사카테고리번호
	private String eventname; // 행사이름
	private String eventaddr; // 행사도로명주소
	private String eventplace; // 행사장소명
	private String eventcontent; // 행사내용
	private Date eventstart; // 행사시작일
	private Date eventend; // 행사종료일
	private String eventlat; // 행사주소경도
	private String eventlong; // 행사주소위도
	private int eventhit; // 행사조회수
	private String eventlink; // 행사링크
	private String eventfname; // 행사사진
	private String eventticket; // 행사티켓예매링크
	private int eventprice; // 행사티켓가격
	private String eventstate; // 행사진행상태
	private String parkplace; // 주차장여부
	private String state; //공연 진행 상태
}
