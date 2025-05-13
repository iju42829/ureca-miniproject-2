package com.ureca.miniproject.friend.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ureca.miniproject.friend.entity.Friend;
import com.ureca.miniproject.friend.entity.FriendId;
import com.ureca.miniproject.friend.entity.Status;



@Repository
public interface FriendRepository extends JpaRepository<Friend, FriendId> {
	//crud 생성 완료

	Boolean existsByFriendIdAndStatus(FriendId friendId,Status status);
	Boolean existsByFriendIdInviteeIdAndStatus(Long id,Status status);
	
	List<Friend> findByFriendId(FriendId friendId);
	List<Friend> findByFriendIdInviteeIdAndStatus(Long id, Status status);
	List<Friend> findByFriendIdInviteeIdOrFriendIdInviterIdAndStatus(Long id1,Long id2, Status status);
	
	
}
