package com.example.demo.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
@Data
@Entity
@Table(name="event")
public class Event {
	@Id
	private int eventNo;
	
	private int categoryNo;
	private String eventName;
	private String eventAddr;
	private String eventPlace;
	private String eventContent;
	private String eventStart;
	private String eventEnd;
	private String eventContent;
	private String eventLong;
	private String eventLat;
	private int eventHit;
	private String eventLink;
	private String eventFname;
	private String eventTicket;
	private int eventPrice;
	private int eventState;
	private String parkPlace;
	
	/*
	@OneToMany(mappedBy = "event",fetch=FetchType.EAGER, cascade=CascadeType.REMOVE)
	private List<ReviewBoard> reviewBoards;
	
	@OneToMany(mappedBy = "event",fetch=FetchType.EAGER, cascade=CascadeType.REMOVE)
	private List<WishList> wishLists;
	*/
	
}
