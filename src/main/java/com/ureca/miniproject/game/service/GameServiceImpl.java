package com.ureca.miniproject.game.service;

import com.ureca.miniproject.game.entity.GameParticipant;
import com.ureca.miniproject.game.entity.GameRoom;
import com.ureca.miniproject.game.entity.RoomStatus;
import com.ureca.miniproject.game.exception.GameParticipantNotFoundException;
import com.ureca.miniproject.game.exception.GameRoomNotFoundException;
import com.ureca.miniproject.game.repository.GameParticipantRepository;
import com.ureca.miniproject.game.repository.GameRoomRepository;
import com.ureca.miniproject.user.entity.User;
import com.ureca.miniproject.user.exception.UserNotFoundException;
import com.ureca.miniproject.user.repository.UserRepository;
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

    private final GameParticipantRepository gameParticipantRepository;
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
    public void updateDeathStatus(Long roomId, String username) {
        User user = userRepository.findByUserName((username))
                .orElseThrow(()-> new UserNotFoundException(USER_NOT_FOUND));

        GameParticipant gameParticipant = gameParticipantRepository.findByUserAndGameRoom_Id(user, roomId)
                .orElseThrow(() -> new GameParticipantNotFoundException(GAME_PARTICIPANT_NOT_FOUND));

        gameParticipant.setIsAlive(false);
    }
}
