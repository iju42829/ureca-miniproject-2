package com.ureca.miniproject.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ureca.miniproject.user.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	//crud 생성 완료
//	Optional<User> findByEmail(String email);
}
