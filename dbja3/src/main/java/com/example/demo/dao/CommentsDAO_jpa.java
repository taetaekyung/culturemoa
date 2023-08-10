package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Comments;

import jakarta.transaction.Transactional;
@Repository
public interface CommentsDAO_jpa extends JpaRepository<Comments, Integer> {
	//게시물 댓글 조회
	@Query("select c from Comments c where c.board.boardno=?1 order by c.comno desc")
	public List<Comments> findByBoardNo(int boardno);
	
	//댓글 수정하기
	@Modifying
	@Transactional
	@Query(value="update Comments c set c.comcontent=?2 where c.comno=?1",nativeQuery = true)
	public void updateByComno(int comno,String comcontent);
	
	//댓글 번호 조회
	@Query("select max(c.comno)+1 from Comments c")
	public int findByNext();
	
}
