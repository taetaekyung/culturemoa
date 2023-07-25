package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ReviewComment;

public interface ReviewCommentDAO_jpa extends JpaRepository<ReviewComment, Integer> {

}
