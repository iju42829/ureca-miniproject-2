package com.ureca.miniproject.game.service;

import com.ureca.miniproject.config.MyUserDetails;
import com.ureca.miniproject.game.controller.request.CreateRoomRequest;
import com.ureca.miniproject.game.entity.GameParticipant;
import com.ureca.miniproject.game.entity.GameRoom;
import com.ureca.miniproject.game.exception.AlreadyJoinedException;
import com.ureca.miniproject.game.exception.GameRoomNotFoundException;
import com.ureca.miniproject.game.repository.GameParticipantRepository;
import com.ureca.miniproject.game.repository.GameRoomRepository;
import com.ureca.miniproject.game.service.response.CreateGameRoomResponse;
import com.ureca.miniproject.game.service.response.GameRoomDetailResponse;
import com.ureca.miniproject.game.service.response.ListGameRoomResponse;
import com.ureca.miniproject.user.entity.User;
import com.ureca.miniproject.user.exception.UserNotFoundException;
import com.ureca.miniproject.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.ureca.miniproject.common.BaseCode.*;
import static com.ureca.miniproject.game.entity.ParticipantStatus.JOINED;
import static com.ureca.miniproject.game.entity.RoomStatus.WAITING;
import static com.ureca.miniproject.user.entity.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class GameRoomServiceTest {

    @Autowired
    private GameRoomService gameRoomService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameParticipantRepository gameParticipantRepository;

    @Autowired
    private GameRoomRepository gameRoomRepository;

    @Test
    @DisplayName("게임방 생성 성공")
    void createGameRoom() {
        // given
        User user = getTestUser();

        userRepository.save(user);

        CreateRoomRequest createRoomRequest = getCreateRoomRequestTest();
        MyUserDetails myUserDetails = new MyUserDetails(user.getUserName(), null, user.getEmail(), null);

        // when
        CreateGameRoomResponse gameRoom = gameRoomService.createGameRoom(createRoomRequest, myUserDetails);

        // then
        assertThat(gameRoom.getRoomId()).isNotNull().isGreaterThan(0);
    }

    @Test
    @DisplayName("존재하지 않는 유저로 게임방 생성 시 예외 발생")
    void createGameRoomNotFoundUser() {
        // given
        CreateRoomRequest createRoomRequest = getCreateRoomRequestTest();
        MyUserDetails myUserDetails = new MyUserDetails("testName", null, "test@test.com", null);

        // when - then
        Assertions.assertThatThrownBy(() -> gameRoomService.createGameRoom(createRoomRequest, myUserDetails))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage(USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("이미 게임에 참가 중인 유저는 게임방 생성 시 예외 발생")
    void createGameRoomExistsGameParticipant() {
        // given
        User user = getTestUser();
        userRepository.save(user);

        CreateRoomRequest createRoomRequest = getCreateRoomRequestTest();
        MyUserDetails myUserDetails = new MyUserDetails(user.getUserName(), null, user.getEmail(), null);
        CreateGameRoomResponse gameRoomResponse = gameRoomService.createGameRoom(createRoomRequest, myUserDetails);

        GameRoom gameRoom = gameRoomRepository.findById(gameRoomResponse.getRoomId()).orElse(null);

        GameParticipant participant = GameParticipant.builder()
                .user(user)
                .gameRoom(gameRoom)
                .status(JOINED)
                .isAlive(true)
                .build();

        gameParticipantRepository.save(participant);

        // when - then
        assertThatThrownBy(() -> gameRoomService.createGameRoom(new CreateRoomRequest(), myUserDetails))
                .isInstanceOf(AlreadyJoinedException.class)
                .hasMessage(GAME_PARTICIPANT_ALREADY_JOINED.getMessage());
    }

    @Test
    @DisplayName("모든 대기중인 게임룸 조회")
    void listGameRoom() {
        // given
        User user = getTestUser();
        userRepository.save(user);

        CreateRoomRequest createRoomRequest = getCreateRoomRequestTest();
        MyUserDetails myUserDetails = new MyUserDetails(user.getUserName(), null, user.getEmail(), null);
        CreateGameRoomResponse gameRoomResponse = gameRoomService.createGameRoom(createRoomRequest, myUserDetails);

        // when
        ListGameRoomResponse listGameRoomResponse = gameRoomService.listGameRoom();

        // then
        assertThat(listGameRoomResponse.getRooms().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("게임방 나가기")
    void leaveGameRoom() {
        // given
        User user = getTestUser();
        userRepository.save(user);

        CreateRoomRequest createRoomRequest = getCreateRoomRequestTest();
        MyUserDetails myUserDetails = new MyUserDetails(user.getUserName(), null, user.getEmail(), null);
        CreateGameRoomResponse gameRoomResponse = gameRoomService.createGameRoom(createRoomRequest, myUserDetails);

        // when
        gameRoomService.leaveGameRoom(gameRoomResponse.getRoomId(), myUserDetails);

        // then
        GameRoom gameRoom = gameRoomRepository.findById(gameRoomResponse.getRoomId())
                .orElseThrow(() -> new GameRoomNotFoundException(GAME_ROOM_NOT_FOUND));

        assertThat(gameRoom.getCurrentPlayer()).isEqualTo(0);
    }

    @Test
    @DisplayName("게임방 삭제")
    void removeGameRoom() {
        // given
        User user = getTestUser();
        userRepository.save(user);

        CreateRoomRequest createRoomRequest = getCreateRoomRequestTest();
        MyUserDetails myUserDetails = new MyUserDetails(user.getUserName(), null, user.getEmail(), null);
        CreateGameRoomResponse gameRoomResponse = gameRoomService.createGameRoom(createRoomRequest, myUserDetails);

        // when
        gameRoomService.removeGameRoom(gameRoomResponse.getRoomId(), myUserDetails);

        // then
        GameRoom gameRoom = gameRoomRepository.findById(gameRoomResponse.getRoomId()).orElse(null);
        assertThat(gameRoom).isNull();
    }

    @Test
    @DisplayName("게임룸 상세 조회")
    void getGameRoomDetail() {
        // given
        User user = getTestUser();
        userRepository.save(user);

        CreateRoomRequest createRoomRequest = getCreateRoomRequestTest();
        MyUserDetails myUserDetails = new MyUserDetails(user.getUserName(), null, user.getEmail(), null);
        CreateGameRoomResponse gameRoomResponse = gameRoomService.createGameRoom(createRoomRequest, myUserDetails);

        // when
        GameRoomDetailResponse gameRoomDetail = gameRoomService.getGameRoomDetail(gameRoomResponse.getRoomId(), myUserDetails);

        // then
        assertThat(gameRoomDetail)
                .extracting(GameRoomDetailResponse::getTitle, GameRoomDetailResponse::getGameRoomStatus, GameRoomDetailResponse::getMaxPlayer,
                        GameRoomDetailResponse::getCurrentPlayer, GameRoomDetailResponse::getIsHost)
                .containsExactly("testTitle", WAITING.name(), 5, 1, true);

        assertThat(gameRoomDetail.getParticipantResponseList().size()).isEqualTo(1);
    }

    private static CreateRoomRequest getCreateRoomRequestTest() {
        CreateRoomRequest createRoomRequest = new CreateRoomRequest();
        createRoomRequest.setTitle("testTitle");
        createRoomRequest.setMaxPlayer(5);

        return createRoomRequest;
    }

    private static User getTestUser() {
        return User.builder()
                .userName("test")
                .email("test@test.com")
                .password("test")
                .role(USER)
                .isOnline(true)
                .build();
    }
}
