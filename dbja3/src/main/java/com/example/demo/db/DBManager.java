package com.example.demo.db;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.example.demo.entity.Opentalk;
import com.example.demo.vo.EventVO;
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
	//----opentalkMapper
	//모든 채팅 목록 가져오기
	public static List<OpentalkVO> findAllTalk(){
		SqlSession session=sqlSessionFactory.openSession();
		List<OpentalkVO> list=null;
		list=session.selectList("talk.findAllTalk");
		session.close();
		return list;
	}
	//채팅창에 업데이트 되지 않은 채팅 가져오기
	public static List<OpentalkVO> findByNo(HashMap<String, Integer> map){
		SqlSession session=sqlSessionFactory.openSession();
		List<OpentalkVO> list=null;
		list=session.selectList("talk.findByNo",map);
		session.close();
		return list;
	}
	//----eventMapper
	//전체 event top15
	public static List<EventVO> findTop(){
		SqlSession session=sqlSessionFactory.openSession();
		List<EventVO>list=null;
		list=session.selectList("event.findTop");
		session.close();
		return list;
	}
	//행사 지역에 따른 top15
	public static List<EventVO> findByArea(String area){
		SqlSession session=sqlSessionFactory.openSession();
		List<EventVO>list=null;
		list=session.selectList("event.findByArea",area);
		session.close();
		return list;
	}
}












