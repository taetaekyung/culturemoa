package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Event;

public interface EventDAO_jpa extends JpaRepository<Event, Integer> {

}
