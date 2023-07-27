package com.example.demo.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.db.DBManager;
import com.example.demo.entity.Opentalk;
import com.example.demo.vo.OpentalkVO;

@Repository
public class opentalkDAO_mb {
	
	public List<OpentalkVO> findAllTalk(){
		return DBManager.findAllTalk();
	}
	public List<OpentalkVO> findByNo(HashMap<String, Integer> map){
		return DBManager.findByNo(map);
	}
}
