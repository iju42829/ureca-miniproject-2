package com.ureca.miniproject.game.service;

import com.ureca.miniproject.config.MyUserDetails;
import com.ureca.miniproject.game.controller.request.EndGameRequest;
import com.ureca.miniproject.game.entity.*;
import com.ureca.miniproject.game.exception.GameParticipantNotFoundException;
import com.ureca.miniproject.game.repository.GameParticipantRepository;
import com.ureca.miniproject.game.repository.GameResultRepository;
import com.ureca.miniproject.game.repository.GameRoomRepository;
import com.ureca.miniproject.game.service.response.GameResultResponse;
import com.ureca.miniproject.game.service.response.ListGameResultResponse;
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

import static com.ureca.miniproject.common.BaseCode.GAME_PARTICIPANT_NOT_FOUND;
import static com.ureca.miniproject.game.entity.GameResultStatus.WIN;
import static com.ureca.miniproject.game.entity.RoomStatus.FINISHED;
import static com.ureca.miniproject.game.entity.RoomStatus.WAITING;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class GameServiceTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRoomRepository gameRoomRepository;

    @Autowired
    private GameParticipantRepository gameParticipantRepository;

    @Autowired
    private GameResultRepository gameResultRepository;
    
    @Autowired
    private static CommonCodeRepository commonCodeRepository; 
    
    @Test
    @DisplayName("게임 시작 시 참가자 역할이 부여된다")
    void startGame() {
        // given
        User user1 = getUserTest("test", "test@test.com", "test");
        User user2 = getUserTest("test1", "test1@test.com", "test1");

        userRepository.saveAll(List.of(user1, user2));

        GameParticipant gameParticipant1 = GameParticipant.builder()
                .user(user1)
                .status(ParticipantStatus.JOINED)
                .isAlive(true)
                .build();
        GameRoom gameRoom = GameRoom.createGameRoom(user1, "testTitle", WAITING, 5, gameParticipant1);
        gameRoomRepository.save(gameRoom);

        GameParticipant gameParticipant2 = GameParticipant.builder()
                .user(user2)
                .gameRoom(gameRoom)
                .status(ParticipantStatus.JOINED)
                .isAlive(true)
                .build();
        gameRoom.getParticipants().add(gameParticipant2);
        gameParticipantRepository.save(gameParticipant2);

        // when
        gameService.startGame(gameRoom.getId());

        // then
        GameParticipant result1 = gameParticipantRepository.findById(gameParticipant1.getId())
                .orElseThrow(() -> new GameParticipantNotFoundException(GAME_PARTICIPANT_NOT_FOUND));

        GameParticipant result2 = gameParticipantRepository.findById(gameParticipant2.getId())
                .orElseThrow(() -> new GameParticipantNotFoundException(GAME_PARTICIPANT_NOT_FOUND));

        assertThat(result1.getRole()).isNotNull();
        assertThat(result2.getRole()).isNotNull();
    }

    @Test
    @DisplayName("게임 종료 시 참가자 상태가 LEFT로 변경되고 게임 결과가 저장된다")
    void endGame() {
        // given
        User user1 = userRepository.save(getUserTest("test1", "test1@test.com", "test1"));

        User user2 = userRepository.save(getUserTest("test2", "test2@test.com", "test2"));

        GameParticipant participant1 = GameParticipant.builder()
                .user(user1)
                .status(ParticipantStatus.JOINED)
                .isAlive(true)
                .build();

        GameRoom gameRoom = GameRoom.createGameRoom(user1, "testTitle", WAITING, 2, participant1);
        gameRoomRepository.save(gameRoom);

        GameParticipant participant2 = GameParticipant.builder()
                .user(user2)
                .gameRoom(gameRoom)
                .status(ParticipantStatus.JOINED)
                .isAlive(true)
                .build();

        gameRoom.getParticipants().add(participant2);
        gameParticipantRepository.save(participant2);

        // when
        EndGameRequest request = new EndGameRequest(List.of(user1.getUserName()));
        gameService.endGame(gameRoom.getId(), request);

        // then
        List<GameParticipant> updatedParticipants = gameParticipantRepository.findAllByGameRoom_Id(gameRoom.getId());
        assertThat(updatedParticipants).allMatch(p -> p.getStatus() == ParticipantStatus.LEFT);

        List<GameResult> results = gameResultRepository.findAll();
        assertThat(results).hasSize(2);

        GameResult result1 = results.stream()
                .filter(r -> r.getUser().getUserName().equals(user1.getUserName()))
                .findFirst()
                .orElseThrow();
        assertThat(result1.getStatus()).isEqualTo(WIN);

        GameResult result2 = results.stream()
                .filter(r -> r.getUser().getUserName().equals(user2.getUserName()))
                .findFirst()
                .orElseThrow();
        assertThat(result2.getStatus()).isEqualTo(GameResultStatus.LOSE);
    }

    @Test
    @DisplayName("참가자의 사망 처리 로직이 정상 동작한다.")
    void updateDeathStatus() {
        User user = userRepository.save(getUserTest("test1", "test1@test.com", "test1"));

        GameParticipant participant = GameParticipant.builder()
                .user(user)
                .status(ParticipantStatus.JOINED)
                .isAlive(true)
                .build();

        GameRoom gameRoom = GameRoom.createGameRoom(user, "deathTestRoom", WAITING, 5, participant);
        gameRoomRepository.save(gameRoom);

        // when
        gameService.updateDeathStatus(gameRoom.getId(), user.getUserName());

        // then
        GameParticipant updated = gameParticipantRepository.findById(participant.getId()).orElseThrow();
        assertThat(updated.getIsAlive()).isFalse();
    }

    @Test
    @DisplayName("게임 결과 조회 시 참여한 게임 결과가 정상적으로 반환된다")
    void listGameResult() {
        // given
        User host = userRepository.save(getUserTest("test1", "test1@test.com", "test1"));

        User player = userRepository.save(getUserTest("test2", "test2@test.com", "test2"));

        GameParticipant participant = GameParticipant.builder()
                .user(player)
                .status(ParticipantStatus.LEFT)
                .isAlive(false)
                .build();

        GameRoom gameRoom = GameRoom.createGameRoom(host, "testTitle", FINISHED, 5, participant);
        gameRoomRepository.save(gameRoom);

        GameResult gameResult = GameResult.builder()
                .gameRoom(gameRoom)
                .user(player)
                .status(WIN)
                .build();

        gameParticipantRepository.save(participant);
        gameResultRepository.save(gameResult);

        // when
        MyUserDetails userDetails = new MyUserDetails(player.getUserName(), null, player.getEmail(), null);
        ListGameResultResponse result = gameService.listGameResult(userDetails);

        // then
        assertThat(result.getGameResults()).hasSize(1);

        GameResultResponse response = result.getGameResults().get(0);
        assertThat(response.getGameName()).isEqualTo(gameRoom.getTitle());
        assertThat(response.getHostUsername()).isEqualTo(host.getUserName());
        assertThat(response.getResultStatus()).isEqualTo(WIN.name());
        assertThat(response.getEndTime()).isNotNull();
    }


    private static User getUserTest(String username, String email, String password) {
    	List<Code> roles = commonCodeRepository.findByGroupCodes(List.of("010")); //010 : userRole
        return User.builder()
                .userName(username)
                .email(email)
                .password(password)
                .role(roles.get(0).getCodeName())
                .isOnline(true)
                .build();
    }
}
