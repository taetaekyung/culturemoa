package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Comments;

public interface CommentsDAO_jpa extends JpaRepository<Comments, Integer> {

}
