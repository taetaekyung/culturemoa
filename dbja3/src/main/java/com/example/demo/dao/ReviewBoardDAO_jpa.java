package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Reviewboard;

@Repository
public interface ReviewBoardDAO_jpa extends JpaRepository<Reviewboard, Integer> {

}
