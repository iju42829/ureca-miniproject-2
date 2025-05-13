package com.ureca.miniproject.game.repository;

import com.ureca.miniproject.game.entity.GameParticipant;
import com.ureca.miniproject.game.entity.GameRoom;
import com.ureca.miniproject.game.exception.GameParticipantNotFoundException;
import com.ureca.miniproject.user.entity.User;
import com.ureca.miniproject.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ureca.miniproject.common.BaseCode.GAME_PARTICIPANT_NOT_FOUND;
import static com.ureca.miniproject.game.entity.ParticipantStatus.JOINED;
import static com.ureca.miniproject.game.entity.RoomStatus.WAITING;
import static com.ureca.miniproject.user.entity.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class GameParticipantRepositoryTest {
    @Autowired
    private GameParticipantRepository gameParticipantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRoomRepository gameRoomRepository;

    @Test
    @DisplayName("User와 Status로 존재 여부를 확인한다.")
    void existsByUserAndStatus() {
        // given
        User user = User.builder()
                .userName("test")
                .email("test@test.com")
                .password("test")
                .role(USER)
                .isOnline(true)
                .build();

        userRepository.save(user);

        GameParticipant gameParticipant = GameParticipant.builder()
                .user(user)
                .status(JOINED)
                .isAlive(true)
                .build();

        GameRoom waitingRoom = GameRoom.createGameRoom(user, "testTitle", WAITING, 5, gameParticipant);
        gameRoomRepository.save(waitingRoom);

        // when
        boolean exists = gameParticipantRepository.existsByUserAndStatus(user, JOINED);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("roomId로 모든 참가자 조회")
    void findAllByGameRoomId() {
        // given
        User user = User.builder()
                .userName("test")
                .email("test@test.com")
                .password("test")
                .role(USER)
                .isOnline(true)
                .build();

        userRepository.save(user);

        GameParticipant gameParticipant = GameParticipant.builder()
                .user(user)
                .status(JOINED)
                .isAlive(true)
                .build();

        GameRoom room = GameRoom.createGameRoom(user, "testTitle", WAITING, 5, gameParticipant);
        gameRoomRepository.save(room);

        // when
        List<GameParticipant> participants = gameParticipantRepository.findAllByGameRoom_Id(room.getId());

        // then
        assertThat(participants).hasSize(1);
    }

    @Test
    @DisplayName("User와 Status로 참가자 조회")
    void findByUserAndStatus() {
        // given
        User user = User.builder()
                .userName("test")
                .email("test@test.com")
                .password("test")
                .role(USER)
                .isOnline(true)
                .build();

        userRepository.save(user);

        GameParticipant gameParticipant = GameParticipant.builder()
                .user(user)
                .status(JOINED)
                .isAlive(true)
                .build();

        GameRoom room = GameRoom.createGameRoom(user, "testTitle", WAITING, 5, gameParticipant);
        gameRoomRepository.save(room);

        GameParticipant result = gameParticipantRepository.findByUserAndStatus(user, JOINED)
                .orElseThrow(() -> new GameParticipantNotFoundException(GAME_PARTICIPANT_NOT_FOUND));

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("User와 GameRoom으로 참가자 삭제")
    void deleteByUserAndGameRoom() {
        // given
        User user = User.builder()
                .userName("test")
                .email("test@test.com")
                .password("test")
                .role(USER)
                .isOnline(true)
                .build();

        userRepository.save(user);

        GameParticipant gameParticipant = GameParticipant.builder()
                .user(user)
                .status(JOINED)
                .isAlive(true)
                .build();

        GameRoom room = GameRoom.createGameRoom(user, "testTitle", WAITING, 5, gameParticipant);
        gameRoomRepository.save(room);

        // when
        gameParticipantRepository.deleteByUserAndGameRoom(user, room);

        // then
        GameParticipant result = gameParticipantRepository.findById(gameParticipant.getId()).orElse(null);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("User와 roomId로 참가자 조회")
    void findByUserAndGameRoomId() {
        // given
        User user = User.builder()
                .userName("test")
                .email("test@test.com")
                .password("test")
                .role(USER)
                .isOnline(true)
                .build();

        userRepository.save(user);

        GameParticipant gameParticipant = GameParticipant.builder()
                .user(user)
                .status(JOINED)
                .isAlive(true)
                .build();

        GameRoom room = GameRoom.createGameRoom(user, "testTitle", WAITING, 5, gameParticipant);
        gameRoomRepository.save(room);

        // when
        GameParticipant result = gameParticipantRepository.findByUserAndGameRoom_Id(user, room.getId())
                .orElseThrow(() -> new GameParticipantNotFoundException(GAME_PARTICIPANT_NOT_FOUND));

        // then
        assertThat(result).isNotNull();
    }
}
