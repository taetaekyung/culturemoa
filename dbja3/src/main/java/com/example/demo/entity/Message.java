package com.example.demo.entity;

import java.util.Date;

import jakarta.persistence.Column;
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
	
    @Column(name = "is_deleted_by_sender_boolean")
    private boolean isDeletedBySender;

    @Column(name = "is_deleted_by_receiver_boolean")
    private boolean isDeletedByReceiver;
}
