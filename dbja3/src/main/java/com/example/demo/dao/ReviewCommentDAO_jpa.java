package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.ReviewComment;
@Repository
public interface ReviewCommentDAO_jpa extends JpaRepository<ReviewComment, Integer> {

}
