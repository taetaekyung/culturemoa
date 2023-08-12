package com.example.demo.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name="Likeboard")
public class Likeboard {
	@Id
	private int likeno;
	
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
	@JoinColumn(name="boardno",insertable = true, updatable = true)
	private Board board;
	
	public void setBoardno(int boardno) {
		if(this.board==null) {
			this.board = new Board();
		}
		this.board.setBoardno(boardno);
	}
	
	public int getBoardno() {
		return this.board.getBoardno();
	}
	
	@ManyToOne
	@JoinColumn(name="reviewno",insertable = true, updatable = true)
	private Reviewboard reviewboard;
	
	public void setReviewno(int reviewno) {
		if(this.reviewboard==null) {
			this.reviewboard = new Reviewboard();
		}
		this.reviewboard.setReviewno(reviewno);
	}
	
	public int getReviewno() {
		return this.reviewboard.getReviewno();
	}
}
