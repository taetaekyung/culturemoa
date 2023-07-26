package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Member;
@Repository
public interface MemberDAO_jpa extends JpaRepository<Member, String> {

}
