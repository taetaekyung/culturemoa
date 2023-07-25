package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Member;

public interface MemberDAO_jpa extends JpaRepository<Member, String> {

}
