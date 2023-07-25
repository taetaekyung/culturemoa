package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="FAQ")
public class FAQ {
	@Id
	private int faqNo;
	
	private String FAQtitle;
	private String FAQcontent;
}
