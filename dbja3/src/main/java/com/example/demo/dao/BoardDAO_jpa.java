package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Board;

@Repository
public interface BoardDAO_jpa extends JpaRepository<Board, Integer> {

	@Query(value = "select * from Board where bcategory=?1", nativeQuery=true)
	public List<Board> findByBcategory(String bcategory);
}
