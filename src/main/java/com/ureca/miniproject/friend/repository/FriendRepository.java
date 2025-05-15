package com.ureca.miniproject.friend.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ureca.miniproject.friend.entity.Friend;
import com.ureca.miniproject.friend.entity.FriendId;
import com.ureca.miniproject.friend.entity.Status;





@Repository
public interface FriendRepository extends JpaRepository<Friend, FriendId> {
	//crud 생성 완료


	Boolean existsByFriendIdAndStatus(FriendId friendId,Status status);
	Boolean existsByFriendIdInviteeIdAndStatus(Long id,Status status);
	Boolean existsByFriendIdInviterIdAndStatus(Long id,Status status);
	

//	List<Friend> findByFriendIdInviteeIdOrFriendIdInviterIdAndStatus(Long id1,Long id2, Status status);
	//괄호가 명확하게 설정되지 않아서 jpql(아래)로 변경 
	@Query("SELECT f FROM Friend f WHERE f.friendId.invitee.id = :userId AND f.status = :status")
	List<Friend> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Status status);
	
	
	@Query("SELECT f FROM Friend f WHERE (f.friendId.invitee.id = :userId OR f.friendId.inviter.id = :userId) AND f.status = :status")
	List<Friend> findFriendsByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Status status);
	
	
	
	@Query("SELECT f FROM Friend f WHERE (f.friendId.invitee.email = :userEmail AND f.friendId.inviter.email = :friendEmail) OR (f.friendId.inviter.email = :userEmail AND f.friendId.invitee.email = :friendEmail)")
	List<Friend> findFriendsByEmail(@Param("userEmail") String userEmail ,@Param("friendEmail") String friendEmail);
	
	
	@Query("SELECT f FROM Friend f WHERE ((f.friendId.invitee.email = :userEmail AND f.friendId.inviter.email = :friendEmail) OR (f.friendId.inviter.email = :userEmail AND f.friendId.invitee.email = :friendEmail)) AND f.status = :status")
	List<Friend> findInvitesRelatedToMe(@Param("userEmail") String userEmail ,@Param("friendEmail") String friendEmail,@Param("status") Status status);				
	
}
