package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.entity.Board;
import com.example.demo.entity.Event;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Event;

@Repository
public interface EventDAO_jpa extends JpaRepository<Event, Integer> {
	//크롤링한 숫자 조회에 사용
	@Query("select count(*) from Event where eventname=?1")
	int countByEventname(String eventname);
	//크롤링한 숫자 조회에 사용
	@Query("select nvl(max(eventno),1) from Event")
	int getEventNo();	
	
	// 이벤트 번호로 이벤트를 조회하는 메소드 추가
    Event findByEventno(int eventno);
    
    //카테고리로만 조회
    Page<Event> findByCategoryno(int categoryno, Pageable pageable);
    
    //지역,카테고리로 조회
    Page<Event> findByEventaddrContainingAndCategoryno(String area, int categoryNo, Pageable pageable);
    
    // 키워드 검색
	@Query(value="select * from Event where eventname like CONCAT(CONCAT('%', ?1), '%') or eventcontent like CONCAT(CONCAT('%', ?1), '%') order by eventstart desc", nativeQuery=true)
	public List<Event> findByKeyword(String keyword);
	
	//지역,카테고리,날짜 조회
    @Query("SELECT e FROM Event e " +
           "WHERE :eventDate BETWEEN e.eventstart AND e.eventend " +
           "AND e.eventaddr LIKE %:area% " +
           "AND e.categoryno = :categoryNo")
    Page<Event> findByEventDateAndAreaContainingAndCategoryNo(
            Date eventDate, String area, int categoryNo, Pageable pageable);
    
    //카테고리,날짜 조회
    @Query("SELECT e FROM Event e " +
           "WHERE :eventDate BETWEEN e.eventstart AND e.eventend " +
           "AND e.categoryno = :categoryNo")
    Page<Event> findByEventDateContainingAndCategoryNo(
            Date eventDate, int categoryNo, Pageable pageable);
  
}
