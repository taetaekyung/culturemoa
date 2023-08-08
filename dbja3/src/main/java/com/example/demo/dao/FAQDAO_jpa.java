package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.FAQ;
@Repository
public interface FAQDAO_jpa extends JpaRepository<FAQ, Integer> {

	@Query(value="select * from FAQ where faqtitle like CONCAT(CONCAT('%', ?1), '%') or faqcontent like CONCAT(CONCAT('%', ?1), '%')", nativeQuery=true)
	public List<FAQ> findByKeyword(String keyword);
}
