package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ReviewBoard;

public interface ReviewBoardDAO_jpa extends JpaRepository<ReviewBoard, Integer> {

}
