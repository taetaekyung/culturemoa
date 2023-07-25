package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Message;

public interface MessageDAO_jpa extends JpaRepository<Message, Integer> {

}
