package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Message;
@Repository
public interface MessageDAO_jpa extends JpaRepository<Message, Integer> {
	@Query(value = "select * from Message where id=?1", nativeQuery = true)
	List<Message> findByReceiveId(String id);
	
	@Query(value = "select * from Message where mid=?1", nativeQuery = true)
	List<Message> findBySendId(String id);
}