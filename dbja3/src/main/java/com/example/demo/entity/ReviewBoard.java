package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="reviewBoard")
public class ReviewBoard {
	@Id
	private int reviewNo;
}
