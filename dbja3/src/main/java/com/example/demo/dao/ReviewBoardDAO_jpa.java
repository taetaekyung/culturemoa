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
public interface ReviewBoardDAO_jpa extends JpaRepository<Reviewboard, Integer> {
	//조회수 업데이트
	@Modifying
	@Transactional
	@Query(value="update Reviewboard r set r.reviewhit=?1 where r.reviewno=?2", nativeQuery = true)
	public int updateHit(int like,int reviewno);
	
	//좋아요 개수 업데이트
	@Modifying
	@Transactional
	@Query(value="update Reviewboard r set r.reviewlike=?1 where r.reviewno=?2", nativeQuery = true)
	public int plusLike(int like,int reviewno);
	
	//REVIEWNO로 찾기
	 @Query("select r from Reviewboard r where r.reviewno = ?1")
	 public Reviewboard findByNo(int reviewno);
	 
	 //삭제
	 @Modifying 
	 @Transactional
	 @Query("delete Reviewboard r where r.reviewno=?1")
	 public int deleteByNo(int reviewno);
	 
	 //다음 reviewno찾기
	 @Query("select nvl(Max(r.reviewno),0)+1 from Reviewboard r ")
	 public int findNextNo();
	 
	 @Query(value = "select * from Reviewboard where reviewtitle like CONCAT(CONCAT('%', :keyword), '%') or reviewcontent like CONCAT(CONCAT('%', :keyword), '%')", nativeQuery=true)
	 public Page<Reviewboard> findByBcategory(@Param("keyword") String keyword, Pageable pageable);

	 @Query(value = "select count(*) from Reviewboard ", nativeQuery = true)
	 public int getTotalRecord();
	
	 @Query(value = "select count(*) from Reviewboard where (reviewtitle like '%?1%' or reviewcontent like '%?1%')" , nativeQuery = true)
	 public int getTotalRecord(String keyword);
 
     @Query(value="select * from Reviewboard where reviewtitle like CONCAT(CONCAT('%', ?1), '%') or reviewcontent like CONCAT(CONCAT('%', ?1), '%') order by regdate desc", nativeQuery=true)
	 public List<Reviewboard> findByKeyword(String keyword);
	 
}
