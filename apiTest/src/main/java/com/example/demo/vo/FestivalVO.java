package com.example.demo.vo;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="festival")
public class FestivalVO {
	@Id
	private String eventNm; // 행사명
	private String opar; // 장소
	private String eventCo; // 행사내용
	private String eventStartDate; // 행사 시작일
	private String eventEndDate; // 행사 종료일
	private String phoneNumber; // 전화번호
	private String homepageUrl; // 행사 홈페이지
	private String advantkInfo; // 예매정보
	private String prkplceYn; // 주차장여부
	private String rdnmadr; // 도로명주소
	private String latitude; // 경도
	private String longitude; // 위도
	
}
