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
import lombok.Data;

@Entity
@Data
@Table(name="reviewboard")
public class Reviewboard {
	@Id
	private Integer reviewno;
	
	private String reviewtitle;
	private String reviewcontent;
	private int reviewhit;
	
	@ManyToOne
	@JoinColumn(name="id",insertable = true, updatable = true)
	private Member member;
	
	@ManyToOne
	@JoinColumn(name="eventno",insertable = true, updatable = true)
	private Event event;
	
	private Date regdate;
	private Integer reviewlike;
	
	@OneToMany(mappedBy = "reviewboard", cascade=CascadeType.REMOVE)
	private List<ReviewComment> reviewcomments = new ArrayList<>();
	
	public void setId(String id) {
		 if (this.member == null) {
		        this.member = new Member();
		    }
		 this.member.setId(id);
	}
	
	public String getMemberId() {
        if (this.member == null) {
        	this.member=new Member();
        }
        return this.member.getId();
    }
	
	public void setEventNo(int eventNo) {
		if(this.event==null) {
			this.event=new Event();
		}
		this.event.setEventno(eventNo);
	}
	
	public int getEventno() {
        if (this.event == null) {
        	this.event=new Event();
        }
        return this.event.getEventno();
    }
}
