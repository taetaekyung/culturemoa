package com.example.demo.dao;

import java.util.HashMap;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.WishList;

public interface WishListDAO_jpa extends JpaRepository<WishList, HashMap<String, Object>> {

}
