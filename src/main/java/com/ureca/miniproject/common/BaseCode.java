package com.ureca.miniproject.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BaseCode {
	// COMMON
	INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR_500",HttpStatus.INTERNAL_SERVER_ERROR,"예기치 못한 오류가 발생했습니다"),
    STATUS_OK("STATUS_OK_200", HttpStatus.OK, "서버가 정상적으로 동작 중입니다."),
    STATUS_AUTHENTICATED("STATUS_AUTHENTICATED_200", HttpStatus.OK, "로그인된 사용자입니다."),
    STATUS_UNAUTHORIZED("STATUS_UNAUTHORIZED_401", HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),

    // USER
    USER_CREATE_SUCCESS("CREATE_USER_201", HttpStatus.CREATED, "회원 정보가 성공적으로 등록되었습니다."),
    USER_FIND_SUCCESS("FIND_USER_201", HttpStatus.CREATED, "회원 정보를 성공적으로 가져왔습니다."),
    USER_ALREADY_EXIST("ALREADY_EXIST_USER_409", HttpStatus.CONFLICT, "동일한 이메일로 회원가입된 유저가 존재합니다."),
    USER_NOT_FOUND("NOT_FOUND_USER_404", HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    //FRIEND
    FRIEND_LIST_SUCCESS("LIST_FRIEND_201", HttpStatus.CREATED, "친구 리스트업이 성공적으로 완료되었습니다."),    
    FRIEND_DELETE_SUCCESS("DELETE_FRIEND_201", HttpStatus.CREATED, "친구 삭제가 성공적으로 완료되었습니다."),    
    FRIEND_UPDATE_SUCCESS("UPDATE_FRIEND_201", HttpStatus.CREATED, "친구 수정이 성공적으로 완료되었습니다."),    
    FRIEND_INVITE_SUCCESS("INVITE_FRIEND_201", HttpStatus.CREATED, "친구 신청이 성공적으로 완료되었습니다."),    
    INVITE_ALREADY_EXIST("INVITE_FRIEND_409", HttpStatus.CONFLICT, "이미 진행중인 초대가 있거나 이미 친구입니다."),    
    INVITE_SELF_DECLINED("INVITE_FRIEND_406", HttpStatus.NOT_ACCEPTABLE, "본인을 친구 추가하시면 안됩니다."),    
        
    
    // GAME_ROOM
    GAME_ROOM_CREATE_SUCCESS("CREATE_GAME_ROOM_201", HttpStatus.CREATED, "게임방이 성공적으로 생성되었습니다."),
    GAME_ROOM_LIST_READ_SUCCESS("READ_GAME_ROOM_LIST_200", HttpStatus.OK, "게임방 목록 조회에 성공했습니다."),
    GAME_ROOM_NOT_FOUND("NOT_FOUND_GAME_ROOM_404", HttpStatus.NOT_FOUND, "게임방을 찾을 수 없습니다."),
    GAME_ROOM_LEAVE_SUCCESS("LEAVE_GAME_ROOM_200", HttpStatus.OK, "게임방에서 나가기에 성공했습니다."),
    GAME_ROOM_NOT_WAITING("GAME_ROOM_NOT_WAITING_400", HttpStatus.BAD_REQUEST, "게임방이 대기 상태가 아닙니다."),
    GAME_ROOM_DELETE_NOT_ALLOWED("DELETE_GAME_ROOM_FORBIDDEN_403", HttpStatus.FORBIDDEN, "게임방 삭제 권한이 없습니다."),
    GAME_ROOM_DELETE_SUCCESS("DELETE_GAME_ROOM_200", HttpStatus.OK, "게임방이 성공적으로 삭제되었습니다."),
    GAME_ROOM_CAPACITY_EXCEEDED("GAME_ROOM_CAPACITY_EXCEEDED_400", HttpStatus.BAD_REQUEST, "게임방 참여 인원이 최대치를 초과했습니다."),
    GAME_ROOM_START_SUCCESS("START_GAME_ROOM_200", HttpStatus.OK, "게임이 성공적으로 시작되었습니다."),
    GAME_ROOM_NOT_ENOUGH_PARTICIPANTS("GAME_ROOM_NOT_ENOUGH_PARTICIPANTS_400", HttpStatus.BAD_REQUEST, "게임을 시작하려면 최소 3명 이상의 참여자가 필요합니다."),

    // GAME_PARTICIPANT
    GAME_PARTICIPANT_ALREADY_JOINED("ALREADY_JOINED_GAME_PARTICIPANT_409", HttpStatus.CONFLICT, "이미 참여 중인 게임방이 있습니다."),
    GAME_PARTICIPANT_CREATE_SUCCESS("CREATE_GAME_PARTICIPANT_201", HttpStatus.CREATED, "게임 참여에 성공했습니다."),
    GAME_PARTICIPANT_LIST_READ_SUCCESS("READ_LIST_GAME_PARTICIPANT_200", HttpStatus.OK, "게임 참여자 목록 조회에 성공했습니다."),
    GAME_PARTICIPANT_READ_JOINED_SUCCESS("READ_JOINED_GAME_PARTICIPANT_204", HttpStatus.NO_CONTENT, "현재 참여 중인 게임방 조회에 성공했습니다."),
    GAME_PARTICIPANT_NOT_FOUND("NOT_FOUND_GAME_PARTICIPANT_404", HttpStatus.NOT_FOUND, "게임 참가자를 찾을 수 없습니다."),
    GAME_PARTICIPANT_DEATH_SUCCESS("PROCESS_GAME_PARTICIPANT_DEATH_200", HttpStatus.OK, "게임 참가자 죽음 처리가 성공했습니다."),

    // GAME_RESULT
    GAME_RESULT_CREATE_SUCCESS("CREATE_GAME_RESULT_201", HttpStatus.CREATED, "게임 전적 저장에 성공했습니다."),
    GAME_RESULT_LIST_READ_SUCCESS("READ_LIST_GAME_RESULT_200", HttpStatus.OK, "게임 전적 목록 조회에 성공했습니다."),

    // GAME
    GAME_END_STATUS_CHECK_SUCCESS("CHECK_GAME_END_STATUS_200", HttpStatus.OK, "게임 종료 여부 확인에 성공했습니다.");


    private final String code;
    private final HttpStatus status;
    private final String message;
}
