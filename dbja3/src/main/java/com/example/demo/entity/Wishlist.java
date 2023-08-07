package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="Wishlist")
public class Wishlist {
	@Id
	private int wishno;
	
	@ManyToOne
	@JoinColumn(name="id",insertable = true, updatable = true)
	private Member member;
	
	public void setMemberId(String memberId) {
		if(this.member==null) {
			this.member = new Member();
		}
		this.member.setId(memberId);
	}
	
	public String getMemberId() {
		return this.member.getId();
	}

	@ManyToOne
	@JoinColumn(name="eventno",insertable = true, updatable = true)
	private Event event;
	
	public void setEventEventno(int eventno) {
		if(this.event==null) {
			this.event = new Event();
		}
		this.event.setEventno(eventno);
	}
	
	public int getEventEventno() {
		return this.event.getEventno();
	}
}
