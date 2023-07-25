package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.FAQ;
@Repository
public interface FAQDAO_jpa extends JpaRepository<FAQ, Integer> {

}
