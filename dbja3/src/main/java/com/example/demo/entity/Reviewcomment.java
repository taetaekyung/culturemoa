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
@Table(name="Reviewcomment")
public class Reviewcomment {
	@Id
	private int rcomno;
	
	private String rcomcontent;
	private Date regdate;

	
	@ManyToOne
	@JoinColumn(name="id",insertable = true, updatable = true)
	private Member member;
	
	@ManyToOne
	@JoinColumn(name="reviewno",insertable = true, updatable = true)
	private Reviewboard reviewboard;
}
