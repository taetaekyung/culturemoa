package com.example.demo.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
@Table(name="opentalk")
public class Opentalk {
	@Id
	private int talkno;
	private String talkcontent;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="id",insertable = true, updatable = true) // "id"는 Member 테이블의 PK 컬럼명
    private Member member;
	
	private Date regdate;
	
	public void setMemberId(String memberId) {
		 if (this.member == null) {
		        this.member = new Member();
		    }
		 this.member.setId(memberId);
	    }
}
