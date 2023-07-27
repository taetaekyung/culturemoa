package com.example.demo.db;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.example.demo.entity.Opentalk;
import com.example.demo.vo.OpentalkVO;


public class DBManager {
	public static SqlSessionFactory sqlSessionFactory;
	static {
		try {
			String resource = "com/example/demo/db/sqlMapConfig.xml";
			InputStream inputStream = Resources.getResourceAsStream(resource);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		}catch (Exception e) {
			System.out.println("예외발생:"+e.getMessage());
		}
	}
	//-----------------------------경선
	//opentalkMapper
	public static List<OpentalkVO> findAllTalk(){
		SqlSession session=sqlSessionFactory.openSession();
		List<OpentalkVO> list=null;
		list=session.selectList("talk.findAllTalk");
		session.close();
		return list;
	}
	
	public static List<OpentalkVO> findByNo(HashMap<String, Integer> map){
		SqlSession session=sqlSessionFactory.openSession();
		List<OpentalkVO> list=null;
		list=session.selectList("talk.findByNo",map);
		session.close();
		return list;
	}
}












