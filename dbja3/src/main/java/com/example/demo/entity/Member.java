package com.example.demo.entity;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="member")
public class Member {
	@Id
	private String id;
	
	private String pwd;
	private String name;
	private String birth;
	private String email;
	private String gender;  //'남성', '여성'
	private String role;    //'admin','user'
	
	@OneToMany(mappedBy = "member",fetch=FetchType.EAGER, cascade=CascadeType.REMOVE)
	private List<Message> messages;
	
	@OneToMany(mappedBy = "member",fetch=FetchType.EAGER, cascade=CascadeType.REMOVE)
	private List<Comments> comments;
	
	@OneToMany(mappedBy = "member",fetch=FetchType.EAGER, cascade=CascadeType.REMOVE )
	private List<ReviewBoard> reviewBoards;
	
	@OneToMany(mappedBy = "member",fetch=FetchType.EAGER, cascade=CascadeType.REMOVE )
	private List<ReviewComment> reviewComments;
	
	@OneToMany(mappedBy = "member",fetch=FetchType.EAGER, cascade=CascadeType.REMOVE )
	private List<WishList> wishLists;
	
}
