package com.ureca.miniproject.game.repository;

import com.ureca.miniproject.game.entity.GameParticipant;
import com.ureca.miniproject.game.entity.GameRoom;
import com.ureca.miniproject.game.entity.ParticipantStatus;
import com.ureca.miniproject.game.entity.RoomStatus;
import com.ureca.miniproject.groupcode.entity.Code;
import com.ureca.miniproject.groupcode.repository.CommonCodeRepository;
import com.ureca.miniproject.user.entity.User;
import com.ureca.miniproject.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
class GameRoomRepositoryTest {

    @Autowired
    private GameRoomRepository gameRoomRepository;

    @Autowired
    private GameParticipantRepository gameParticipantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommonCodeRepository commonCodeRepository; 
    
    @AfterEach
    void tearDown() {
        gameParticipantRepository.deleteAllInBatch();
        gameRoomRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("RoomStatus로 게임방 목록을 조회합니다.")
    void findAllByRoomStatus() {
        // given
    	List<Code> roles = commonCodeRepository.findByGroupCodes(List.of("010")); //010 : userRole
        User user = User.builder()
                .userName("test")
                .email("test@test.com")
                .password("test")
                .role(roles.get(0).getCodeName())
                .isOnline(true)
                .build();

        userRepository.save(user);

        GameParticipant gameParticipant = GameParticipant.builder()
                .user(user)
                .status(ParticipantStatus.JOINED)
                .isAlive(true)
                .build();

        GameRoom waitingRoom = GameRoom.createGameRoom(user, "testTitle", RoomStatus.WAITING, 5, gameParticipant);
        gameRoomRepository.save(waitingRoom);

        // when
        List<GameRoom> gameRooms = gameRoomRepository.findAllByRoomStatus(RoomStatus.WAITING);

        // then
        assertThat(gameRooms)
                .hasSize(1)
                .extracting(GameRoom::getTitle, GameRoom::getRoomStatus, GameRoom::getMaxPlayer, GameRoom::getCurrentPlayer)
                .containsExactly(tuple(waitingRoom.getTitle(), waitingRoom.getRoomStatus(), waitingRoom.getMaxPlayer(), 1));
    }

}
