package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Comments;
import com.example.demo.entity.Reviewcomment;

import jakarta.transaction.Transactional;
@Repository
public interface ReviewCommentDAO_jpa extends JpaRepository<Reviewcomment, Integer> {
	//게시물 댓글 조회
	@Query("select c from Reviewcomment c where c.reviewboard.reviewno=?1 order by c.rcomno desc")
	public List<Reviewcomment> findByReviewBoardNo(int reviewno);
	
	//댓글 수정하기
	@Modifying
	@Transactional
	@Query(value="update Reviewcomment c set c.rcomcontent=?2 where c.rcomno=?1",nativeQuery = true)
	public void updateByRcomno(int rcomno,String rcomcontent);
	
	//댓글 번호 조회
	@Query("select max(c.rcomno)+1 from Reviewcomment c")
	public int findByNext();
}
