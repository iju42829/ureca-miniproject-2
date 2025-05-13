package com.ureca.miniproject.friend.service;


import static com.ureca.miniproject.common.BaseCode.INVITE_ALREADY_EXIST;
import static com.ureca.miniproject.common.BaseCode.USER_NOT_FOUND;
import static com.ureca.miniproject.friend.entity.Status.ACCEPTED;
import static com.ureca.miniproject.friend.entity.Status.WAITING;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ureca.miniproject.config.MyUserDetails;
import com.ureca.miniproject.friend.controller.request.InviteFriendRequest;
import com.ureca.miniproject.friend.controller.request.UpdateFriendRequest;
import com.ureca.miniproject.friend.entity.Friend;
import com.ureca.miniproject.friend.entity.FriendId;
import com.ureca.miniproject.friend.entity.Status;
import com.ureca.miniproject.friend.exception.InviteAlreadyExistException;
import com.ureca.miniproject.friend.exception.UserNotFoundException;
import com.ureca.miniproject.friend.repository.FriendRepository;
import com.ureca.miniproject.friend.service.response.InviteFriendResponse;
import com.ureca.miniproject.friend.service.response.ListFriendStatusResponse;
import com.ureca.miniproject.friend.service.response.UpdateFriendResponse;
import com.ureca.miniproject.user.entity.User;
import com.ureca.miniproject.user.repository.UserRepository;

import jakarta.transaction.Transactional;
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
		
		FriendId friendId = FriendId.builder().invitee(invitee).inviter(inviter).build();
		if(friendRepository.existsByFriendIdAndStatus(friendId,WAITING) ||
		   friendRepository.existsByFriendIdAndStatus(friendId,ACCEPTED)
			) {
			throw new InviteAlreadyExistException(INVITE_ALREADY_EXIST);
		}
		
		//상호추가 방지
		//본인이 invitee로 되어 있고, waiting인 상태의 friend가 있으면  throw
		User me = userRepository.findByEmail(myUserDetails.getEmail()).get();	
		if(friendRepository.existsByFriendIdInviteeIdAndStatus(me.getId(), Status.WAITING)) {
			throw new InviteAlreadyExistException(INVITE_ALREADY_EXIST);
		}

		Friend friend = friendRepository.save(
						Friend.builder()
							.friendId(friendId)
							.status(WAITING)
							.build()				
				);		
		friendRepository.flush();			

		return new InviteFriendResponse(friend.getFriendId().getInvitee().getUserName(), friend.getFriendId().getInviter().getUserName());
	}
	

	@Override
	public UpdateFriendResponse updateFriend(UpdateFriendRequest updateFriendRequest) {
		
		
		
		User inviter = userRepository.findByEmail(updateFriendRequest.getInviterEmail())
	            .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));						
		userRepository.save(inviter);		
		userRepository.flush();
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();			
		User invitee = userRepository.findByEmail(myUserDetails.getEmail()).orElseThrow();		
		userRepository.save(invitee);
		userRepository.flush();
		
		
		FriendId friendId1 = FriendId.builder().invitee(invitee).inviter(inviter).build();		
		
		Status beforeStatus = friendRepository.findById(friendId1).get().getStatus();
		
		friendRepository.save(
					Friend.builder()
						  .friendId(friendId1)
						  .status(updateFriendRequest.getStatusDesired())						  
						  .build()
				);
			
		Status afterStatus = friendRepository.findById(friendId1).get().getStatus();
		return new UpdateFriendResponse(beforeStatus,afterStatus );
	}

	@Override
	public ListFriendStatusResponse listFriendStatus(Status statusDesired) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();	
		User me = userRepository.findByEmail(myUserDetails.getEmail()).get();		
		System.out.println(me.getId());
		List<Friend> friends = friendRepository.findByFriendIdInviteeIdOrFriendIdInviterIdAndStatus(me.getId(),me.getId(),statusDesired);
		
		
		for(Friend friend : friends) {
			System.out.println("id" + friend.getFriendId().getInviter().getEmail());
		}
		return new ListFriendStatusResponse(friends);
	}


	@Override
	public ListFriendStatusResponse listFriend() {
		// TODO Auto-generated method stub
		return null;
	}


//	@Override
//	public ListFriendStatusResponse listFriend() {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();	
//		User me = userRepository.findByEmail(myUserDetails.getEmail()).get();		
//		
//		//일단 login된 user가 invitee이든, inviter이든 상관없이 가져오기
//		List<Friend> friendInfos = friendRepository.findByFriendIdInviteeIdOrFriendIdInviterIdAndStatus(me.getId(),me.getId(),Status.ACCEPTED);
//		//본인이 invitee가 아닌것
//		List<User> friends = new ArrayList<User>(); 
//		for(Friend friendInfo : friendInfos) {
//			//본인이 inviter면 invitee를 추가, invitee면 inviter를 추가
//			String myEmail = me.getEmail();
//			String inviteeEmail = friendInfo.getFriendId().getInvitee().getEmail();
//			String inviterEmail = friendInfo.getFriendId().getInviter().getEmail();
//			if(myEmail.equals( inviteeEmail)) {
//				
//			}else if(myEmail.equals(inviterEmail)){
//				
//			}else {
//				throw new 
//			}
//				
////			System.out.println("id" + friend.getFriendId().getInviter().getEmail());
//		}
//		return new ListFriendStatusResponse(friends);
//	}
	
	
	
	

}
