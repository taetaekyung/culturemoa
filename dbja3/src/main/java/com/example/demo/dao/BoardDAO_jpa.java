package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Board;

public interface BoardDAO_jpa extends JpaRepository<Board, Integer> {

}
