package com.ureca.miniproject.friend.service;


import static com.ureca.miniproject.common.BaseCode.INVITE_ALREADY_EXIST;
import static com.ureca.miniproject.common.BaseCode.INVITE_SELF_DECLINED;
import static com.ureca.miniproject.common.BaseCode.USER_NOT_FOUND;
import static com.ureca.miniproject.friend.entity.Status.ACCEPTED;
import static com.ureca.miniproject.friend.entity.Status.DECLINED;
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
import com.ureca.miniproject.friend.exception.InviteSelfException;
import com.ureca.miniproject.friend.exception.UserNotFoundException;
import com.ureca.miniproject.friend.repository.FriendRepository;
import com.ureca.miniproject.friend.service.response.InviteFriendResponse;
import com.ureca.miniproject.friend.service.response.ListFriendResponse;
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
		
		if(inviter.getEmail().equals(invitee.getEmail())) {
			throw new InviteSelfException(INVITE_SELF_DECLINED);
		}
		
		
		FriendId friendId = FriendId.builder().invitee(invitee).inviter(inviter).build();
		User user = userRepository.findByEmail(myUserDetails.getEmail()).get();	
			//login user가 invitee이거나, inviter면서 accepted이거나 waiting된게 있으면 throw
			//상호추가 방지
		   	//본인이 invitee로 되어 있고, waiting인 상태의 friend가 있으면  throw
//		if(friendRepository.existsByFriendIdInviteeIdAndStatus(user.getId(),WAITING) ||
////		   friendRepository.existsByFriendIdInviteeIdAndStatus(me.getId(),ACCEPTED)||		   
////		   friendRepository.existsByFriendIdInviterIdAndStatus(me.getId(),ACCEPTED)||  //-> 이건데...35와 34사이에 accept 된거를 잡아내서 35에서 33으로 신청하는 것도 막힘. 즉, 상대방의 id를 고려해야하는데,,본인만 고려. 중복된거 처리 같은 경우도 양측다 고려를 해야함/
//		   friendRepository.existsByFriendIdInviterIdAndStatus(user.getId(),WAITING)){
//			throw new InviteAlreadyExistException(INVITE_ALREADY_EXIST);
//		}
		
		//login user가 invitee이거나, inviter면서 accepted이거나 waiting된게 있으면 throw
		//상호추가 방지
	   	//본인이 invitee로 되어 있고, waiting인 상태의 friend가 있으면  throw
		System.out.println(friendRepository.findInvitesRelatedToMe(user.getEmail(),invitee.getEmail(), ACCEPTED));
		if(!friendRepository.findInvitesRelatedToMe(user.getEmail(),invitee.getEmail(), WAITING).isEmpty() ||
		   !friendRepository.findInvitesRelatedToMe(user.getEmail(),invitee.getEmail(), ACCEPTED).isEmpty() ){
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
		User user = userRepository.findByEmail(myUserDetails.getEmail()).orElseThrow();		
		userRepository.save(user);
		userRepository.flush();

		FriendId friendId = FriendId.builder().invitee(user).inviter(inviter).build();		
		
		Status beforeStatus = friendRepository.findById(friendId).get().getStatus();
		
		friendRepository.save(
					Friend.builder()
						  .friendId(friendId)
						  .status(updateFriendRequest.getStatusDesired())						  
						  .build()
				);		
		
		Status afterStatus = friendRepository.findById(friendId).get().getStatus();
		return new UpdateFriendResponse(beforeStatus,afterStatus );
	}

	@Override

	public ListFriendStatusResponse listFriendStatus(Status statusDesired) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();	
		User user = userRepository.findByEmail(myUserDetails.getEmail()).get();
		//user가 inviter 거나, invitee인 두가지 상황을 가져오는게 아니라, user가 invitee인 상황만
		List<Friend> friends = friendRepository.findByUserIdAndStatus(user.getId(),statusDesired);						
		
		return new ListFriendStatusResponse(friends);
	}

	
	@Override
	public ListFriendResponse listFriend() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();	
		User me = userRepository.findByEmail(myUserDetails.getEmail()).get();		
		
		//일단 login된 user가 invitee이든, inviter이든 상관없이 가져오기
		List<Friend> friendInfos = friendRepository.findFriendsByUserIdAndStatus(me.getId(),Status.ACCEPTED);
		//본인이 invitee가 아닌것
		List<User> friends = new ArrayList<User>(); 
		for(Friend friendInfo : friendInfos) {
			//본인이 inviter면 invitee를 추가, invitee면 inviter를 추가
			String myEmail = me.getEmail();
			String inviteeEmail = friendInfo.getFriendId().getInvitee().getEmail();
			String inviteeUserName = friendInfo.getFriendId().getInvitee().getUserName();
			String inviterEmail = friendInfo.getFriendId().getInviter().getEmail();
			String inviterUserName = friendInfo.getFriendId().getInviter().getUserName();
			User friend = null;
			if(myEmail.equals( inviteeEmail)) {
				//todo : user말고 다른 dto로 보내기 (user에 필요 없는 정보 빼기)
				friend = User.builder()
								  .email(inviterEmail)
								  .userName(inviterUserName)								  
								  .build();				
			}else if(myEmail.equals(inviterEmail)){
				friend = User.builder()
						  .email(inviteeEmail)
						  .userName(inviteeUserName)								  
						  .build();
			}else {
				 throw new UserNotFoundException(USER_NOT_FOUND);
			}
				
			friends.add(friend);
		}
		
		return new ListFriendResponse(friends);
	}


	@Override
	public Boolean deleteFriend(String emailToDelete) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();	
		User user = userRepository.findByEmail(myUserDetails.getEmail()).get();
		List<Friend> friendInfos = friendRepository.findFriends(user.getEmail(), emailToDelete);

		try {
			friendInfos.forEach(friend -> {
			    friendRepository.delete(friend);
			});
		}catch(Exception e){
			throw e;
		}
		
		return true;
	}
	
	
	
	
	
	
	

}
