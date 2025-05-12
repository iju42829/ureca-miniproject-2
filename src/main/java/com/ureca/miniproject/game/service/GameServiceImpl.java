package com.ureca.miniproject.game.service;

import com.ureca.miniproject.config.MyUserDetails;
import com.ureca.miniproject.game.controller.request.EndGameRequest;
import com.ureca.miniproject.game.entity.*;
import com.ureca.miniproject.game.exception.GameParticipantNotFoundException;
import com.ureca.miniproject.game.exception.GameRoomNotFoundException;
import com.ureca.miniproject.game.repository.GameParticipantRepository;
import com.ureca.miniproject.game.repository.GameResultRepository;
import com.ureca.miniproject.game.repository.GameRoomRepository;
import com.ureca.miniproject.game.service.response.GameResultResponse;
import com.ureca.miniproject.game.service.response.ListGameResultResponse;
import com.ureca.miniproject.user.entity.User;
import com.ureca.miniproject.user.exception.UserNotFoundException;
import com.ureca.miniproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.ureca.miniproject.common.BaseCode.*;
import static com.ureca.miniproject.game.entity.ParticipantRole.*;
import static com.ureca.miniproject.game.entity.ParticipantStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameParticipantRepository gameParticipantRepository;
    private final GameResultRepository gameResultRepository;
    private final GameRoomRepository gameRoomRepository;
    private final UserRepository userRepository;

    @Override
    public void startGame(Long roomId) {
        GameRoom gameRoom = gameRoomRepository.findById(roomId).orElseThrow(() -> new GameRoomNotFoundException(GAME_ROOM_NOT_FOUND));

        gameRoom.setRoomStatus(RoomStatus.PLAYING);

        List<GameParticipant> participants = gameRoom.getParticipants();

        int mafiaIndex = ThreadLocalRandom.current()
                .nextInt(participants.size());

        for (int i = 0; i < participants.size(); i++) {
            GameParticipant p = participants.get(i);
            if (i == mafiaIndex) {
                p.setRole(MAFIA);
            } else {
                p.setRole(CITIZEN);
            }
        }
    }

    @Override
    public void endGame(Long roomId, EndGameRequest endGameRequest) {
        GameRoom gameRoom = gameRoomRepository.findById(roomId)
                .orElseThrow(() -> new GameRoomNotFoundException(GAME_ROOM_NOT_FOUND));

        gameRoom.setRoomStatus(RoomStatus.FINISHED);

        List<GameParticipant> participants = gameParticipantRepository.findAllByGameRoom_Id(roomId);

        for (GameParticipant p : participants)
            p.setStatus(LEFT);

        List<GameResult> results = participants.stream()
                .map(p -> {
                    GameResultStatus outcome = endGameRequest.getWinners().contains(p.getUser().getUserName())
                            ? GameResultStatus.WIN
                            : GameResultStatus.LOSE;
                    return GameResult.builder()
                            .gameRoom(gameRoom)
                            .user(p.getUser())
                            .status(outcome)
                            .build();
                })
                .toList();

        gameResultRepository.saveAll(results);
    }

    @Override
    public void updateDeathStatus(Long roomId, String username) {
        User user = userRepository.findByUserName((username))
                .orElseThrow(()-> new UserNotFoundException(USER_NOT_FOUND));

        GameParticipant gameParticipant = gameParticipantRepository.findByUserAndGameRoom_Id(user, roomId)
                .orElseThrow(() -> new GameParticipantNotFoundException(GAME_PARTICIPANT_NOT_FOUND));

        gameParticipant.setIsAlive(false);
    }

    @Override
    public ListGameResultResponse listGameResult(MyUserDetails myUserDetails) {
        User user = userRepository.findByUserName((myUserDetails.getUsername()))
                .orElseThrow(()-> new UserNotFoundException(USER_NOT_FOUND));

        List<GameResult> gameResult = gameResultRepository.findAllByUser(user);

        ListGameResultResponse listGameResultResponse = new ListGameResultResponse(new ArrayList<>());

        for (GameResult result : gameResult) {
            GameResultResponse response = GameResultResponse.builder()
                    .gameName(result.getGameRoom().getTitle())
                    .hostUsername(result.getGameRoom().getHostUser().getUserName())
                    .endTime(result.getCreatedDate())
                    .resultStatus(result.getStatus().name())
                    .build();

            listGameResultResponse.getGameResults().add(response);
        }

        return listGameResultResponse;
    }
}
