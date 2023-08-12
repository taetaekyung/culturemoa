package com.example.demo.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Likeboard;

import jakarta.transaction.Transactional;


@Repository
public interface LikeBoardDAO_jpa extends JpaRepository<Likeboard, Integer> {
	
	@Query(value = "select count(*) from Likeboard l where l.id=?1 and l.boardno=?2", nativeQuery = true)
	public int countByIdAndBoardno(String id, int boardno);
	
	@Query(value = "select count(*) from Likeboard l where l.id=?1 and l.reviewno=?2", nativeQuery = true)
	public int countByIdAndReviewno(String id, int reviewno);
	
	@Query(value="select nvl(max(likeno)+1,1) from Likeboard", nativeQuery = true)
	int getLikeno();
	
	@Modifying
	@Transactional
	@Query(value = "delete from Likeboard where id=?1 and boardno=?2", nativeQuery = true)
	public void deleteByIdAndBoardno(String id, int boardno);
}
