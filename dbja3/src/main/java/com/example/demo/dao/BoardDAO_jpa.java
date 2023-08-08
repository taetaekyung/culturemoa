package com.example.demo.dao;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Board;
import com.example.demo.entity.Reviewboard;

import jakarta.transaction.Transactional;

@Repository
public interface BoardDAO_jpa extends JpaRepository<Board, Integer> {
	 //삭제
	 @Modifying 
	 @Transactional
	 @Query(value="delete board b where b.boardno=?1", nativeQuery = true)
	 public int deleteByNo(int boardno);
	 
	//조회수 업데이트
	@Modifying
	@Transactional
	@Query(value="update Board b set b.boardhit=?1 where b.boardno=?2", nativeQuery = true)
	public int updateHit(int like,int boardno);
	
	//좋아요 개수 업데이트
	@Modifying
	@Transactional
	@Query(value="update Board b set b.boardlikes=?1 where b.boardno=?2", nativeQuery = true)
	public int updatefreeLike(int like,int boardno);

	//REVIEWNO로 찾기
	 @Query("select b from Board b where b.boardno = ?1")
	 public Board findByNo(int boardno);
	 
	 //다음 reviewno찾기
	 @Query("select nvl(Max(b.boardno),0)+1 from Board b ")
	 public int findNextNo();
	 
	 @Query(value = "select b from Board b where b.bcategory=?1 and (b.boardtitle like CONCAT(CONCAT('%', ?2), '%') or b.boardcontent like CONCAT(CONCAT('%', ?2), '%'))")
	 public Page<Board> findByBcategory(String bcategory, String keyword, Pageable pageable);


	@Query(value = "select count(*) from Board where bcategory=?1", nativeQuery = true)
	public int getTotalRecord(String bcategory);
	
	@Query(value = "select count(*) from Board where bcategory=?1 and (boardtitle like '%?2%' or boardcontent like '%?2%')" , nativeQuery = true)
	public int getTotalRecord(String bcategory, String keyword);
	
	//전체 게시물에서 최신순 10개
	@Query(value="select a.* from ("
			+ "select rownum as rown, boardno,boardtitle,regdate, bcategory,boardcontent,boardhit,boardlikes,id from Board order by regdate desc)a "
			+ "where a.rown between 1 and 10", nativeQuery = true)
	public List<Board> findAll();
	
	@Query(value="select * from Board where boardtitle like CONCAT(CONCAT('%', ?1), '%') or boardcontent like CONCAT(CONCAT('%', ?1), '%') order by regdate desc", nativeQuery=true)
	public List<Board> findByKeyword(String keyword);
	
}
