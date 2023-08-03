package com.example.demo.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Member;
import com.example.demo.entity.Message;

@Repository
public interface MemberDAO_jpa extends JpaRepository<Member, String> {
	
	
	Optional<Member> findById(String id);
}
