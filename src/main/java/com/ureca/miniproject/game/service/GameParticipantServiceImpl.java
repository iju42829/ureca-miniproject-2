package com.ureca.miniproject.game.service;

import com.ureca.miniproject.config.MyUserDetails;
import com.ureca.miniproject.game.entity.GameParticipant;
import com.ureca.miniproject.game.entity.GameRoom;
import com.ureca.miniproject.game.exception.GameRoomNotFoundException;
import com.ureca.miniproject.game.mapper.GameParticipantMapper;
import com.ureca.miniproject.game.repository.GameParticipantRepository;
import com.ureca.miniproject.game.repository.GameRoomRepository;
import com.ureca.miniproject.game.service.response.GameParticipantResponse;
import com.ureca.miniproject.game.service.response.GameRoomDetailResponse;
import com.ureca.miniproject.game.service.response.GameRoomResponse;
import com.ureca.miniproject.game.service.response.ParticipantCheckResponse;
import com.ureca.miniproject.user.entity.User;
import com.ureca.miniproject.user.exception.UserNotFoundException;
import com.ureca.miniproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ureca.miniproject.common.BaseCode.GAME_ROOM_NOT_FOUND;
import static com.ureca.miniproject.common.BaseCode.USER_NOT_FOUND;
import static com.ureca.miniproject.game.entity.ParticipantStatus.JOINED;

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
    public GameRoomDetailResponse getGameRoomDetail(Long roomId, MyUserDetails myUserDetails) {
        List<GameParticipantResponse> gameParticipantResponseList = gameParticipantRepository.findAllByGameRoom_Id(roomId).stream()
                .map(gameParticipantMapper::toGameParticipantResponse)
                .toList();

        GameRoom gameRoom = gameRoomRepository.findById(roomId).orElseThrow(() -> new GameRoomNotFoundException(GAME_ROOM_NOT_FOUND));

        User user = userRepository.findByEmail(myUserDetails.getEmail()).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        return GameRoomDetailResponse.builder()
                .title(gameRoom.getTitle())
                .maxPlayer(gameRoom.getMaxPlayer())
                .currentPlayer(gameRoom.getCurrentPlayer())
                .isHost(gameRoom.getHostUser() == user)
                .participantResponseList(gameParticipantResponseList)
                .build();
    }

    @Override
    public ParticipantCheckResponse checkParticipant(MyUserDetails myUserDetails) {
        User user = userRepository.findByEmail(myUserDetails.getEmail()).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        GameParticipant joinedParticipant = gameParticipantRepository.findByUserAndStatus(user, JOINED).orElse(null);

        if (joinedParticipant == null) {
            return null;
        }

        return new ParticipantCheckResponse(joinedParticipant.getGameRoom().getId());
    }
}
