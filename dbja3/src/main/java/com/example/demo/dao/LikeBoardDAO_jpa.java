package com.example.demo.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Likeboard;


@Repository
public interface LikeBoardDAO_jpa extends JpaRepository<Likeboard, Integer> {
	
	 
}
