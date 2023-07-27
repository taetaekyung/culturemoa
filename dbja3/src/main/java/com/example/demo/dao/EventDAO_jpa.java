package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.demo.entity.Event;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Event;

@Repository
public interface EventDAO_jpa extends JpaRepository<Event, Integer> {
	
	@Query("select count(*) from Event where eventname=?1")
	int countByEventname(String eventname);
	
	@Query("select nvl(max(eventno),1) from Event")
	int getEventNo();	
	
	Page<Event> findByCategoryno(int categoryno, Pageable pageable);
}
