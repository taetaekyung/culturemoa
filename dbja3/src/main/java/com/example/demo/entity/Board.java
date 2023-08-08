package com.example.demo.entity;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Entity
@Setter
@Getter
@Table(name="board")
public class Board {
	@Id
	private int boardno;
	private String bcategory;
	private String boardtitle;
	private String boardcontent;
	private int boardhit;

	@ManyToOne(fetch = FetchType.LAZY)
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
	
	private Date regdate;
	private int boardlikes;
	
	@OneToMany(mappedBy = "board", cascade=CascadeType.REMOVE)
	private List<Comments> comments = new ArrayList<>();
	
	@Transient
	private int rown;  
	
}
