package com.ureca.miniproject.game.service;

import com.ureca.miniproject.common.BaseCode;
import com.ureca.miniproject.config.MyUserDetails;
import com.ureca.miniproject.game.entity.GameParticipant;
import com.ureca.miniproject.game.entity.GameRoom;
import com.ureca.miniproject.game.entity.ParticipantStatus;
import com.ureca.miniproject.game.exception.GameRoomNotFoundException;
import com.ureca.miniproject.game.mapper.GameParticipantMapper;
import com.ureca.miniproject.game.repository.GameParticipantRepository;
import com.ureca.miniproject.game.repository.GameRoomRepository;
import com.ureca.miniproject.game.service.response.GameParticipantResponse;
import com.ureca.miniproject.game.service.response.GameRoomResponse;
import com.ureca.miniproject.game.service.response.ListGameParticipantResponse;
import com.ureca.miniproject.game.service.response.ListGameRoomResponse;
import com.ureca.miniproject.user.entity.User;
import com.ureca.miniproject.user.exception.UserNotFoundException;
import com.ureca.miniproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ureca.miniproject.common.BaseCode.*;
import static com.ureca.miniproject.game.entity.ParticipantStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class GameParticipantServiceImpl implements GameParticipantService {

    private final GameParticipantRepository gameParticipantRepository;
    private final GameRoomRepository gameRoomRepository;
    private final UserRepository userRepository;

    private final GameParticipantMapper gameParticipantMapper;

    @Override
    public Long createParticipant(Long roomId, MyUserDetails myUserDetails) {
        GameRoom gameRoom = gameRoomRepository.findById(roomId)
                .orElseThrow(() -> new GameRoomNotFoundException(GAME_ROOM_NOT_FOUND));

        gameRoom.addCurrentPlayer();

        User user = userRepository.findByEmail(myUserDetails.getEmail())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        GameParticipant gameParticipant = GameParticipant.builder()
                .gameRoom(gameRoom)
                .user(user)
                .status(JOINED)
                .isAlive(true)
                .build();

        gameParticipantRepository.save(gameParticipant);

        return gameParticipant.getId();
    }

    @Override
    public ListGameParticipantResponse listGameParticipant(Long roomId) {
        List<GameParticipantResponse> gameParticipantResponseList = gameParticipantRepository.findAllByGameRoom_Id(roomId).stream()
                .map(gameParticipantMapper::toGameParticipantResponse)
                .toList();

        ListGameParticipantResponse listGameParticipantResponse = new ListGameParticipantResponse();

        listGameParticipantResponse.setParticipantResponseList(gameParticipantResponseList);

        return listGameParticipantResponse;
    }
}
