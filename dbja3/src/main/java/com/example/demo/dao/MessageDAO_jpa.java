package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Message;

@Repository
public interface MessageDAO_jpa extends JpaRepository<Message, Integer> {	  
	// 새로운 메소드를 추가하여 페이징된 받은 쪽지들을 가져옴 (삭제 여부 검사 추가)
	@Query(value = "SELECT * FROM message WHERE id = ?1 AND is_deleted_by_receiver_boolean = 0", countQuery = "SELECT count(*) FROM message WHERE id = ?1 AND is_deleted_by_receiver_boolean = 0", nativeQuery = true)
	Page<Message> findPagedReceivedMessages(String id, Pageable pageable);
	
	// 새로운 메소드를 추가하여 페이징된 보낸 쪽지들을 가져옴 (삭제 여부 검사 추가)
	@Query(value = "SELECT * FROM message WHERE mid = ?1 AND is_deleted_by_sender_boolean = 0", countQuery = "SELECT count(*) FROM message WHERE mid = ?1 AND is_deleted_by_sender_boolean = 0", nativeQuery = true)
	Page<Message> findPagedSentMessages(String mid, Pageable pageable);
}