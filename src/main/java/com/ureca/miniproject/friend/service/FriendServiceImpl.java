package com.ureca.miniproject.friend.service;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ureca.miniproject.config.MyUserDetails;
import com.ureca.miniproject.friend.controller.request.InviteFriendRequest;
import com.ureca.miniproject.friend.entity.Friend;
import static com.ureca.miniproject.friend.entity.Status.WAITING;
import com.ureca.miniproject.friend.repository.FriendRepository;
import com.ureca.miniproject.friend.service.response.InviteFriendResponse;
import com.ureca.miniproject.user.entity.Role;
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
		System.out.println(inviteFriendRequest.getInviteeEmail());

		User invitee = userRepository.findByEmail(inviteFriendRequest.getInviteeEmail()).orElseThrow();
		userRepository.save(invitee);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
		String roleStr = myUserDetails.getAuthorities().iterator().next().getAuthority(); // "ROLE_USER"
		Role role = Role.valueOf(roleStr.replace("ROLE_", "")); // "USER" → Role.USER
		User inviter = User.builder()
				.email(myUserDetails.getEmail())
				.userName(myUserDetails.getUsername())				
				.role(role)
				.build();
		userRepository.save(inviter);
		
		Friend friend = friendRepository.save(
						Friend.builder()
							.invitee(invitee)
							.inviter(inviter)
							.status(WAITING)
							.build()				
				);
		
		
		return new InviteFriendResponse(friend.getInvitee().getUserName(), friend.getInviter().getUserName());
	}

}
