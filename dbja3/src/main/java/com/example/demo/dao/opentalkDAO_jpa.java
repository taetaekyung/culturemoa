package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Opentalk;

import jakarta.transaction.Transactional;

@Repository
public interface opentalkDAO_jpa extends JpaRepository<Opentalk, Integer> {
	@Modifying
	@Query(value="insert into Opentalk o(o.talkno,o.talkcontent,o.id,o.regdate) values(:#{#o.talkno},:#{#o.talkcontent},:#{#o.member.id},sysdate)",nativeQuery = true)
	@Transactional
	public void insert(@Param("o") Opentalk o);
	
	@Query("select nvl(max(talkno),0)+1 from Opentalk")
	public int nextNo();

}
