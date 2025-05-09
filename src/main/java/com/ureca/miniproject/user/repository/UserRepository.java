package com.ureca.miniproject.user.repository;

import com.ureca.miniproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	//crud 생성 완료
//	Optional<User> findByEmail(String email);
}
