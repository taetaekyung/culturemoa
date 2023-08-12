package com.example.demo.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Member;
import com.example.demo.entity.Message;

import jakarta.transaction.Transactional;

@Repository
public interface MemberDAO_jpa extends JpaRepository<Member, String> {
    // 닉네임으로 회원을 조회하는 메소드
    Optional<Member> findByNickname(String nickname);
	
	@Modifying
	@Transactional
	@Query(value = "delete from member where id=?1", nativeQuery = true)
	public void deleteById(String id);
	
	@Query(value = "select count(*) from member where id=?1", nativeQuery = true)
	public int countById(String id);
	
	@Query(value = "select count(*) from member where nickname=?1", nativeQuery = true)
	public int countByNickname(String nickname);
	
	Optional<Member> findById(String id);
	
	@Query(value = "select nickname from member where id=?1", nativeQuery = true)
	public String findNicknameById(String id);
	
	//아이디 중복확인
	@Query("select m.id from Member m where m.id=?1")
	public String findId(String id);
	
	//닉네임 중복확인
	@Query("select m.nickname from Member m where m.nickname=?1")
	public String findNickname(String nickname);
}
