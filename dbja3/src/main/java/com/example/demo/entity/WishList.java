package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="WishList")
public class WishList {
	@Id
	private int wishno;
	
	@ManyToOne
	@JoinColumn(name="id",insertable = true, updatable = true)
	private Member member;
	

	@ManyToOne
	@JoinColumn(name="eventno",insertable = true, updatable = true)
	private Event event;
	
}
