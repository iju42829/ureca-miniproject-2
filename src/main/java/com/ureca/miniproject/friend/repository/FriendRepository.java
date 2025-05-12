package com.ureca.miniproject.friend.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ureca.miniproject.friend.entity.Friend;
import com.ureca.miniproject.friend.entity.FriendId;



@Repository
public interface FriendRepository extends JpaRepository<Friend, FriendId> {
	//crud 생성 완료

	Boolean existsByFriendId(FriendId friendId);
	
	List<Friend> findByFriendId(FriendId friendId);
	List<Friend> findByFriendIdInviteeId(Long Id);
	
}
