package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.vo.FestivalVO;

@Repository
public interface FestivalDAO extends JpaRepository<FestivalVO, String> {

}
