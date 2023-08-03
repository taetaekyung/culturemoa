package com.example.demo.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="message")
public class Message {
	@Id
	private int mno;
	
	private String mid; //발신 아이디
	private String mcontent;
	
	@ManyToOne
	@JoinColumn(name="id",insertable = true, updatable = true)
	private Member member;
	private Date regdate;
}
