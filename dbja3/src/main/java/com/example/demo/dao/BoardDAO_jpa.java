package com.example.demo.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Board;

@Repository
public interface BoardDAO_jpa extends JpaRepository<Board, Integer> {

	@Query(value = "select * from Board where bcategory=:bcategory and boardtitle like CONCAT(CONCAT('%', :keyword), '%') or boardcontent like CONCAT(CONCAT('%', :keyword), '%')", nativeQuery=true)
	public Page<Board> findByBcategory(@Param("bcategory") String bcategory,@Param("keyword") String keyword, Pageable pageable);

	@Query(value = "select count(*) from Board where bcategory=?1", nativeQuery = true)
	public int getTotalRecord(String bcategory);
	
	@Query(value = "select count(*) from Board where bcategory=?1 and (boardtitle like '%?2%' or boardcontent like '%?2%')" , nativeQuery = true)
	public int getTotalRecord(String bcategory, String keyword);
}
