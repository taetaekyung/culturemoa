package com.example.demo.entity;

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
@Table(name="event")
public class Event {
	@Id
	private int eventno;
	
	private int categoryno;
	private String eventname;
	private String eventaddr;
	private String eventplace;
	private String eventcontent;
	private String eventstart;
	private String eventend;
	private String eventlong;
	private String eventlat;
	private int eventhit;
	private String eventlink;
	private String eventfname;
	private String eventticket;
	private int eventprice;
	private int eventstate;
	private String parkplace;
	
	/*
	@OneToMany(mappedBy = "event",fetch=FetchType.EAGER, cascade=CascadeType.REMOVE)
	private List<ReviewBoard> reviewBoards;
	
	@OneToMany(mappedBy = "event",fetch=FetchType.EAGER, cascade=CascadeType.REMOVE)
	private List<WishList> wishLists;
	
	*/
}