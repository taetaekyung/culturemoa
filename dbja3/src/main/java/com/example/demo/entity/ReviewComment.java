package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="ReviewComment")
public class ReviewComment {
	@Id
	private int rcomNo;
	
	private String rcomContent;
	
	@ManyToOne
	@JoinColumn(name="id",insertable = true, updatable = true)
	private Member member;
	
	@ManyToOne
	@JoinColumn(name="reviewNo",insertable = true, updatable = true)
	private ReviewBoard reviewBoard;
	
	
}
