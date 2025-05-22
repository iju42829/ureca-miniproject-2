package com.ureca.miniproject.game.repository;

import com.ureca.miniproject.game.entity.*;
import com.ureca.miniproject.groupcode.entity.Code;
import com.ureca.miniproject.groupcode.repository.CommonCodeRepository;
import com.ureca.miniproject.user.entity.User;
import com.ureca.miniproject.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ureca.miniproject.game.entity.GameResultStatus.LOSE;
import static com.ureca.miniproject.game.entity.GameResultStatus.WIN;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class GameResultRepositoryTest {

    @Autowired
    private GameResultRepository gameResultRepository;

    @Autowired
    private GameParticipantRepository gameParticipantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRoomRepository gameRoomRepository;

    @Autowired
    private CommonCodeRepository commonCodeRepository; 
    
    @Test
    @DisplayName("User를 기준으로 게임 결과 조회")
    void findAllByUser() {
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
        GameRoom gameRoom = GameRoom.createGameRoom(user, "testTitle", RoomStatus.FINISHED, 5, gameParticipant);
        gameRoomRepository.save(gameRoom);

        GameResult gameResult1 = GameResult.builder()
                .user(user)
                .gameRoom(gameRoom)
                .status(WIN)
                .build();

        GameResult gameResult2 = GameResult.builder()
                .user(user)
                .gameRoom(gameRoom)
                .status(LOSE)
                .build();

        gameResultRepository.saveAll(List.of(gameResult1, gameResult2));

        // when
        List<GameResult> gameResults = gameResultRepository.findAllByUser(user);

        // then
        assertThat(gameResults)
                .hasSize(2)
                .extracting(GameResult::getStatus)
                .containsExactly(WIN, LOSE);
    }
}
