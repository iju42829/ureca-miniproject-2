package com.ureca.miniproject.friend.service;


import static com.ureca.miniproject.common.BaseCode.INVITE_ALREADY_EXIST;
import static com.ureca.miniproject.common.BaseCode.USER_NOT_FOUND;
import static com.ureca.miniproject.friend.entity.Status.WAITING;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ureca.miniproject.config.MyUserDetails;
import com.ureca.miniproject.friend.controller.request.InviteFriendRequest;
import com.ureca.miniproject.friend.entity.Friend;
import com.ureca.miniproject.friend.entity.FriendId;
import com.ureca.miniproject.friend.exception.InviteAlreadyExistException;
import com.ureca.miniproject.friend.exception.UserNotFoundException;
import com.ureca.miniproject.friend.repository.FriendRepository;
import com.ureca.miniproject.friend.service.response.InviteFriendResponse;
import com.ureca.miniproject.user.entity.User;
import com.ureca.miniproject.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
@Service
@Transactional
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
	
	private final FriendRepository friendRepository;
	private final UserRepository userRepository;
	@Override
	public InviteFriendResponse inviteFriend(InviteFriendRequest inviteFriendRequest) {
		
		User invitee = userRepository.findByEmail(inviteFriendRequest.getInviteeEmail())
		            .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
		
		userRepository.save(invitee);
		
		userRepository.flush();
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();			
		User inviter = userRepository.findByEmail(myUserDetails.getEmail()).orElseThrow();		
		userRepository.save(inviter);
		userRepository.flush();
		
		FriendId friendId1 = FriendId.builder().invitee(invitee).inviter(inviter).build();
		if(friendRepository.existsByFriendId(friendId1)) {
			throw new InviteAlreadyExistException(INVITE_ALREADY_EXIST);
		}
		
		FriendId friendId2 = FriendId.builder().invitee(inviter).inviter(invitee).build();
		if(friendRepository.existsByFriendId(friendId2)) {
			throw new InviteAlreadyExistException(INVITE_ALREADY_EXIST);
		}
		
		Friend friend1 = friendRepository.save(
						Friend.builder()
							.friendId(friendId1)
							.status(WAITING)
							.build()				
				);
		friendRepository.flush();
		Friend friend2 = friendRepository.save(
				Friend.builder()
				.friendId(friendId2)
				.status(WAITING)
				.build()				
				);
		
		friendRepository.flush();

		
		//중복된 친구 추가 방지 -> key를 multikey로 수정 -> done
		//todo : 상호 참조 친구 추가 방지
		//	   : 예외 처리 : 이미 보낸 초대입니다. 상대방이 보낸 초대입니다. 없는 사용자입니다. 
		
		return new InviteFriendResponse(friend1.getFriendId().getInvitee().getUserName(), friend1.getFriendId().getInviter().getUserName());
	}
	
	

}
