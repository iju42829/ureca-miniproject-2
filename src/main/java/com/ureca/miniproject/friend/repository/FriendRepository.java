package com.ureca.miniproject.friend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ureca.miniproject.friend.entity.Friend;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
	//crud 생성 완료
}
