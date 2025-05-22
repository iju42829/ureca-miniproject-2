package com.ureca.miniproject.game.service;

import com.ureca.miniproject.common.BaseCode;
import com.ureca.miniproject.config.MyUserDetails;
import com.ureca.miniproject.game.entity.GameParticipant;
import com.ureca.miniproject.game.entity.GameRoom;
import com.ureca.miniproject.game.entity.ParticipantStatus;
import com.ureca.miniproject.game.entity.RoomStatus;
import com.ureca.miniproject.game.exception.GameRoomNotFoundException;
import com.ureca.miniproject.game.repository.GameParticipantRepository;
import com.ureca.miniproject.game.repository.GameRoomRepository;
import com.ureca.miniproject.game.service.response.ParticipantCheckResponse;
import com.ureca.miniproject.groupcode.entity.Code;
import com.ureca.miniproject.groupcode.repository.CommonCodeRepository;
import com.ureca.miniproject.user.entity.User;
import com.ureca.miniproject.user.exception.UserNotFoundException;
import com.ureca.miniproject.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ureca.miniproject.common.BaseCode.GAME_ROOM_NOT_FOUND;
import static org.assertj.core.api.Assertions.*;


@Transactional
@SpringBootTest
class GameParticipantServiceTest {

    @Autowired
    private GameParticipantRepository gameParticipantRepository;

    @Autowired
    private GameRoomRepository gameRoomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameParticipantService gameParticipantService;
    
    @Autowired
    private CommonCodeRepository commonCodeRepository; 
    
    @Test
    @DisplayName("게임방 참가자 생성에 성공한다.")
    void createParticipant() {
        // given
    	List<Code> roles = commonCodeRepository.findByGroupCodes(List.of("010")); //010 : userRole
        User user = User.builder()
                .userName("test")
                .email("test@test.com")
                .password("test")
                .role(roles.get(0).getCodeName())
                .isOnline(true)
                .build();

        GameParticipant gameParticipant = GameParticipant.builder()
                .user(user)
                .status(ParticipantStatus.JOINED)
                .isAlive(true)
                .build();

        userRepository.save(user);

        GameRoom gameRoom = GameRoom.createGameRoom(user, "testTitle", RoomStatus.WAITING, 5, gameParticipant);
        gameRoomRepository.save(gameRoom);

        MyUserDetails myUserDetails = new MyUserDetails("test", null, "test@test.com", null);

        // when
        Long participant = gameParticipantService.createParticipant(gameRoom.getId(), myUserDetails);

        // then
        assertThat(participant).isNotNull();
        assertThat(gameParticipantRepository.findById(participant).isPresent()).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 게임방이면 예외가 발생한다.")
    void createParticipantNotFoundGameRoom() {
        // given - when - then
        assertThatThrownBy(() -> gameParticipantService.createParticipant(1L, null))
                .isInstanceOf(GameRoomNotFoundException.class)
                .hasMessage(GAME_ROOM_NOT_FOUND.getMessage());

    }

    @Test
    @DisplayName("존재하지 않는 유저이면 예외가 발생한다.")
    void createParticipantNotFoundUser() {
        // given
    	List<Code> roles = commonCodeRepository.findByGroupCodes(List.of("010")); //010 : userRole
        User user = User.builder()
                .userName("test")
                .email("test@test.com")
                .password("test")
                .role(roles.get(0).getCodeName())
                .isOnline(true)
                .build();

        GameParticipant gameParticipant = GameParticipant.builder()
                .user(user)
                .status(ParticipantStatus.JOINED)
                .isAlive(true)
                .build();

        userRepository.save(user);

        GameRoom gameRoom = GameRoom.createGameRoom(user, "testTitle", RoomStatus.WAITING, 5, gameParticipant);
        gameRoomRepository.save(gameRoom);

        MyUserDetails myUserDetails = new MyUserDetails("test1", null, "test1@test.com", null);

        // when - then
        assertThatThrownBy(() -> gameParticipantService.createParticipant(gameRoom.getId(), myUserDetails))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage(BaseCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("참가 중인 유저가 존재하면 해당 게임방 ID를 반환한다.")
    void checkParticipant() {
        // given
    	List<Code> roles = commonCodeRepository.findByGroupCodes(List.of("010")); //010 : userRole
        User user = User.builder()
                .userName("test")
                .email("test@test.com")
                .password("test")
                .role(roles.get(0).getCodeName())
                .isOnline(true)
                .build();

        GameParticipant gameParticipant = GameParticipant.builder()
                .user(user)
                .status(ParticipantStatus.JOINED)
                .isAlive(true)
                .build();

        userRepository.save(user);

        GameRoom gameRoom = GameRoom.createGameRoom(user, "testTitle", RoomStatus.WAITING, 5, gameParticipant);
        gameRoomRepository.save(gameRoom);

        MyUserDetails myUserDetails = new MyUserDetails("test", null, "test@test.com", null);

        // when
        ParticipantCheckResponse participantCheckResponse = gameParticipantService.checkParticipant(myUserDetails);

        // then
        assertThat(participantCheckResponse.getRoomId()).isEqualTo(gameRoom.getId());
    }

    @Test
    @DisplayName("참가 중인 유저가 없으면 null을 반환한다.")
    void checkParticipantNotFoundGameParticipant() {
        // given
    	List<Code> roles = commonCodeRepository.findByGroupCodes(List.of("010")); //010 : userRole
        User user1 = User.builder()
                .userName("test")
                .email("test@test.com")
                .password("test")
                .role(roles.get(0).getCodeName())
                .isOnline(true)
                .build();

        User user2 = User.builder()
                .userName("test1")
                .email("test1@test.com")
                .password("test")
                .role(roles.get(0).getCodeName())
                .isOnline(true)
                .build();

        userRepository.saveAll(List.of(user1, user2));

        GameParticipant gameParticipant = GameParticipant.builder()
                .user(user1)
                .status(ParticipantStatus.JOINED)
                .isAlive(true)
                .build();


        GameRoom gameRoom = GameRoom.createGameRoom(user1, "testTitle", RoomStatus.WAITING, 5, gameParticipant);
        gameRoomRepository.save(gameRoom);

        MyUserDetails myUserDetails = new MyUserDetails("test1", null, "test1@test.com", null);

        // when
        ParticipantCheckResponse participantCheckResponse = gameParticipantService.checkParticipant(myUserDetails);

        // then
        assertThat(participantCheckResponse).isNull();
    }

    @Test
    @DisplayName("존재하지 않는 유저일 경우 예외가 발생한다.")
    void checkParticipantNotFoundUser() {
        // given
        MyUserDetails myUserDetails = new MyUserDetails("test1", null, "test1@test.com", null);

        // when - then
        assertThatThrownBy(() -> gameParticipantService.checkParticipant(myUserDetails))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage(BaseCode.USER_NOT_FOUND.getMessage());
    }
}
