package com.example.demo.entity;

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
@Table(name="reviewBoard")
public class ReviewBoard {
	@Id
	private int reviewNo;
	
	private String reviewTitle;
	private String reivewContent;
	private int reviewhit;
	
	@ManyToOne
	@JoinColumn(name="id",insertable = true, updatable = true)
	private Member member;
	
	@ManyToOne
	@JoinColumn(name="eventNo",insertable = true, updatable = true)
	private Event event;
	
	private String reviewFname;
	
	@OneToMany(mappedBy = "reviewBoard",fetch=FetchType.EAGER, cascade=CascadeType.REMOVE)
	private List<ReviewComment> reviewcomments;
}