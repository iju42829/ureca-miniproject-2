package com.ureca.miniproject.user.service;


import static com.ureca.miniproject.common.BaseCode.GAME_ROOM_NOT_FOUND;
import static com.ureca.miniproject.common.BaseCode.USER_ALREADY_EXIST;
import static com.ureca.miniproject.common.BaseCode.USER_NOT_FOUND;
import static com.ureca.miniproject.game.entity.ParticipantStatus.INVITED;
import static com.ureca.miniproject.game.entity.ParticipantStatus.JOINED;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ureca.miniproject.config.MyUserDetails;
import com.ureca.miniproject.game.entity.GameParticipant;
import com.ureca.miniproject.game.entity.GameRoom;
import com.ureca.miniproject.game.entity.ParticipantStatus;
import com.ureca.miniproject.game.exception.GameRoomNotFoundException;
import com.ureca.miniproject.game.repository.GameParticipantRepository;
import com.ureca.miniproject.game.repository.GameRoomRepository;
import com.ureca.miniproject.groupcode.entity.Code;
import com.ureca.miniproject.groupcode.repository.CommonCodeRepository;
import com.ureca.miniproject.user.controller.request.SignUpRequest;
import com.ureca.miniproject.user.entity.User;
import com.ureca.miniproject.user.exception.UserAlreadyExistException;
import com.ureca.miniproject.user.exception.UserNotFoundException;
import com.ureca.miniproject.user.repository.UserRepository;
import com.ureca.miniproject.user.service.response.InvitesUserResponse;
import com.ureca.miniproject.user.service.response.ListUserResponse;
import com.ureca.miniproject.user.service.response.ProposeUserResponse;
import com.ureca.miniproject.user.service.response.SignUpResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final GameParticipantRepository gameParticipantRepository;
	private final GameRoomRepository gameRoomRepository;
	private final CommonCodeRepository commonCodeRepository;
	
	public SignUpResponse signUp(SignUpRequest signupRequest) {
		User user = null;
		if(userRepository.existsByEmail(signupRequest.getEmail())) {			
			throw(new UserAlreadyExistException(USER_ALREADY_EXIST));
		}		
		
		List<Code> roles = commonCodeRepository.findByGroupCodes(List.of("010")); //010 : userRole
		
		user = userRepository.save(
				User.builder()
					.userName(signupRequest.getUserName())
					.password(passwordEncoder.encode(signupRequest.getPassword()))
					.role(roles.get(0).getCodeKey().getCode()) //userRole : user
					.email(signupRequest.getEmail())											
					.build()
				);			
		
		return new SignUpResponse(user.getUserName(),user.getEmail());
	}
	public ListUserResponse listUsers(Long roomId) {
	    // 전체 유저 목록
	    List<User> allUsers = userRepository.findAll();

	    // 로그인한 유저 정보
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
	    String loginUserEmail = myUserDetails.getEmail();
	    
	    
	    // 해당 게임방 참가자 중 status == JOINED 인 유저만 필터링
	    List<GameParticipant> joinedParticipants = gameParticipantRepository.findAllByGameRoom_Id(roomId).stream()
	        .filter(participant -> participant.getStatus() == ParticipantStatus.JOINED)
	        .toList();

	    // 참가자의 userId 집합
	    Set<Long> joinedUserIds = joinedParticipants.stream()
	        .map(p -> p.getUser().getId())
	        .collect(Collectors.toSet());

	    // 전체 유저 중에서 로그인한 유저, 그리고 이미 게임에 JOINED된 유저는 제외
	    List<User> filteredUsers = allUsers.stream()
	        .filter(user -> !user.getEmail().equals(loginUserEmail)) // 로그인 유저 제외
	        .filter(user -> !joinedUserIds.contains(user.getId()))   // 이미 참가한 유저 제외
	        .collect(Collectors.toList());

	    return new ListUserResponse(filteredUsers);
	}


//	UserResultDto detailUser(int id); // 사용자 상세 조회
//	UserResultDto updateUser(UserDto user); // 사용자 수정
//	void deleteUser(int id); // 사용자 삭제
//	public UserResultDto listUsersUpgrade();
	
	@Override
	public ProposeUserResponse inviteUser(Long roomId,Long userId) {
		GameRoom gameRoom = gameRoomRepository.findById(roomId)
                .orElseThrow(() -> new GameRoomNotFoundException(GAME_ROOM_NOT_FOUND));
		
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        
        
        if(gameParticipantRepository.existsByUserAndStatus(user, JOINED)) {
        	throw(new UserAlreadyExistException(USER_ALREADY_EXIST));
        }
        
        GameParticipant gameParticipant = GameParticipant.builder()
                .gameRoom(gameRoom)
                .user(user)
                .status(INVITED)
                .isAlive(true)
                .build();

        gameParticipantRepository.save(gameParticipant);
        
        return new ProposeUserResponse(gameParticipant.getId());
	}
	
	@Override
	public InvitesUserResponse getInvites() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
		User user = userRepository.findByEmail(myUserDetails.getEmail())
				.orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
		
		List<Long> invites = gameParticipantRepository.findForInvites(user.getId());
		System.out.println(invites.size());
		return new InvitesUserResponse(invites);
	}
	@Override
	public void deleteInvite(Long roomId) {
		
		GameRoom gameRoom = gameRoomRepository.findById(roomId)
				.orElseThrow(() -> new GameRoomNotFoundException(GAME_ROOM_NOT_FOUND));
				
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
		User user = userRepository.findByEmail(myUserDetails.getEmail())
				.orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
		
		
		gameParticipantRepository.deleteByUserAndGameRoomAndStatus(user, gameRoom,INVITED);
	}
	
	@Override
	public ProposeUserResponse acceptInvite(Long roomId) {
		
		GameRoom gameRoom = gameRoomRepository.findById(roomId)
				.orElseThrow(() -> new GameRoomNotFoundException(GAME_ROOM_NOT_FOUND));
		
		gameRoom.addCurrentPlayer();
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
		User user = userRepository.findByEmail(myUserDetails.getEmail())
				.orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
		
		gameRoomRepository.flush();
		userRepository.flush();
		gameParticipantRepository.deleteByUserAndGameRoomAndStatus(user, gameRoom,INVITED);
		GameParticipant gameParticipant = GameParticipant.builder()
				.gameRoom(gameRoom)
				.user(user)
				.status(JOINED)
				.isAlive(true)
				.build();
		
		gameParticipantRepository.save(gameParticipant);
		
		return new ProposeUserResponse(gameParticipant.getId());
	}
}
