package com.example.demo.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="opentalk")
public class Opentalk {
	@Id
	private int talkno;
	private String talkcontent;
	private String id;
	private Date regdate;
}
