package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.User;

public interface UserRepository  extends JpaRepository<User, Long>{
	//JPA에서 기본적으로 제공해주는 CRUD 메서드 말고 이메일로 사용자 객체를 찾는 메서드를 사용하기 때문에 작성해 주었다.
	 Optional<User> findByEmail(String email);
}
