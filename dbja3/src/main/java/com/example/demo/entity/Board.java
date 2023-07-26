package com.example.demo.entity;

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
@Table(name="board")
public class Board {
	@Id
	private int boardno;
	
	private String bcategory;
	private String boardtitle;
	private String boardcontent;
	private String boardfname;
	private int boardhit;
	
	@ManyToOne
	@JoinColumn(name="id",insertable = true, updatable = true)
	private Member member;
	
	private Date regdate;
	
	@OneToMany(mappedBy = "board",fetch=FetchType.EAGER, cascade=CascadeType.REMOVE)
	private List<Comments> comments;
	
}
