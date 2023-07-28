package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Board;

@Repository
public interface BoardDAO_jpa extends JpaRepository<Board, Integer> {

	@Query(value = "select boardno, bcategory, boardcontent, boardtitle, boardfname, id, regdate, boardhit, boardlikes "
			+ "from (select rownum n, boardno, bcategory, boardcontent, boardtitle, boardfname, id, regdate, boardhit, boardlikes "
			+ "from (select * from Board where bcategory=:bcategory and boardtitle like CONCAT(CONCAT('%', :keyword), '%') or boardcontent like CONCAT(CONCAT('%', :keyword), '%'))) a "
			+ "where a.n between :startRecord and :endRecord order by regdate desc", nativeQuery=true)
	public List<Board> findByBcategoryOrderByRegdate(@Param("bcategory") String bcategory,@Param("keyword") String keyword,@Param("startRecord") int startRecord,
			@Param("endRecord") int endRecord);
	
	@Query(value = "select boardno, bcategory, boardcontent, boardtitle, boardfname, id, regdate, boardhit, boardlikes "
			+ "from (select rownum n, boardno, bcategory, boardcontent, boardtitle, boardfname, id, regdate, boardhit, boardlikes "
			+ "from (select * from Board where bcategory=:bcategory and boardtitle like CONCAT(CONCAT('%', :keyword), '%') or boardcontent like CONCAT(CONCAT('%', :keyword), '%'))) a "
			+ "where a.n between :startRecord and :endRecord order by boardhit desc", nativeQuery=true)
	public List<Board> findByBcategoryOrderByBoardhit(@Param("bcategory") String bcategory,@Param("keyword") String keyword,@Param("startRecord") int startRecord,
			@Param("endRecord") int endRecord);
	
	@Query(value = "select count(*) from Board where bcategory=?1", nativeQuery = true)
	public int getTotalRecord(String bcategory);
	
	@Query(value = "select count(*) from Board where bcategory=?1 and (boardtitle like '%?2%' or boardcontent like '%?2%')" , nativeQuery = true)
	public int getTotalRecord(String bcategory, String keyword);
}
