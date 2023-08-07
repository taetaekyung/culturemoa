package com.example.demo.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Opentalk;
import com.example.demo.entity.Wishlist;

import jakarta.transaction.Transactional;
@Repository
public interface WishListDAO_jpa extends JpaRepository<Wishlist, Integer> {
	
	@Query(value = "select count(*) from Wishlist w where w.id=?1 and w.eventno=?2", nativeQuery = true)
	public int countByIdAndEventno(String id, int eventno);
	
	@Query(value = "select nvl(max(w.wishno),1) from Wishlist w", nativeQuery = true)
	public int getWishNo();

	/*
	  @Modifying
	  
	  @Query(
	  value="insert into WishList w (w.wistno,w.id,w.eventno) values(:#{#w.wistno},:#{#w.member.id},:#{#w.event.eventno}"
	  ,nativeQuery = true)
	  
	  @Transactional public void insert(@Param("w") WishList w);
	 */
	
	 //삭제
	 @Modifying 
	 @Transactional
	 @Query(value="delete Wishlist w where w.id=?1 and w.eventno=?2", nativeQuery = true)
	 public void deleteByIdAndEventno(String id, int eventno);
}
