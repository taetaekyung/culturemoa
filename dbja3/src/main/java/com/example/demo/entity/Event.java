package com.example.demo.entity;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="event")
public class Event {
	@Id
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
	private String eventticket; // 행사티켓오픈날짜
	private int eventprice; // 행사티켓가격
	private String parkplace; // 주차장여부
	
	@OneToMany(mappedBy = "event", cascade=CascadeType.REMOVE)
	private List<ReviewBoard> reviewBoards = new ArrayList<>();
	   
	@OneToMany(mappedBy = "event", cascade=CascadeType.REMOVE)
	private List<WishList> wishLists = new ArrayList<>();

}