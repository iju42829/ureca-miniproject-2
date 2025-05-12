package com.ureca.miniproject.game.service;

import com.ureca.miniproject.game.entity.GameParticipant;
import com.ureca.miniproject.game.entity.GameRoom;
import com.ureca.miniproject.game.entity.RoomStatus;
import com.ureca.miniproject.game.exception.GameRoomNotFoundException;
import com.ureca.miniproject.game.repository.GameRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.ureca.miniproject.common.BaseCode.*;
import static com.ureca.miniproject.game.entity.ParticipantRole.*;

@Service
@Transactional
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRoomRepository gameRoomRepository;

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
}
