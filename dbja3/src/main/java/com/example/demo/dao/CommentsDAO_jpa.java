package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Comments;
@Repository
public interface CommentsDAO_jpa extends JpaRepository<Comments, Integer> {

}
