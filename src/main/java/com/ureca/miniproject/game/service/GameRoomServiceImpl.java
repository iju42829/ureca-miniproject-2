package com.ureca.miniproject.game.service;

import com.ureca.miniproject.config.MyUserDetails;
import com.ureca.miniproject.game.controller.request.CreateRoomRequest;
import com.ureca.miniproject.game.entity.GameParticipant;
import com.ureca.miniproject.game.entity.GameRoom;
import com.ureca.miniproject.game.exception.AlreadyJoinedException;
import com.ureca.miniproject.game.exception.GameRoomDeleteForbiddenException;
import com.ureca.miniproject.game.exception.GameRoomNotFoundException;
import com.ureca.miniproject.game.exception.GameRoomNotWaitingException;
import com.ureca.miniproject.game.mapper.GameParticipantMapper;
import com.ureca.miniproject.game.mapper.GameRoomMapper;
import com.ureca.miniproject.game.repository.GameParticipantRepository;
import com.ureca.miniproject.game.repository.GameRoomRepository;
import com.ureca.miniproject.game.service.response.*;
import com.ureca.miniproject.user.entity.User;
import com.ureca.miniproject.user.exception.UserNotFoundException;
import com.ureca.miniproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ureca.miniproject.common.BaseCode.*;
import static com.ureca.miniproject.game.entity.ParticipantStatus.JOINED;
import static com.ureca.miniproject.game.entity.RoomStatus.WAITING;

@Service
@Transactional
@RequiredArgsConstructor
public class GameRoomServiceImpl implements GameRoomService {

    private final GameParticipantRepository gameParticipantRepository;
    private final GameRoomRepository gameRoomRepository;
    private final UserRepository userRepository;

    private final GameRoomMapper gameRoomMapper;
    private final GameParticipantMapper gameParticipantMapper;

    @Override
    public CreateGameRoomResponse createGameRoom(CreateRoomRequest createRoomRequest, MyUserDetails myUserDetails) {
        User user = userRepository.findByEmail(myUserDetails.getEmail())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        Boolean exists = gameParticipantRepository.existsByUserAndStatus(user, JOINED);

        if (exists) {
            throw new AlreadyJoinedException(GAME_PARTICIPANT_ALREADY_JOINED);
        }

        GameParticipant gameParticipant = GameParticipant.builder()
                .user(user)
                .status(JOINED)
                .isAlive(true)
                .build();

        GameRoom gameRoom = GameRoom.createGameRoom(user, createRoomRequest.getTitle(), WAITING, createRoomRequest.getMaxPlayer(), gameParticipant);

        gameRoomRepository.save(gameRoom);

        return new CreateGameRoomResponse(gameRoom.getId());
    }

    @Override
    public ListGameRoomResponse listGameRoom() {
        List<GameRoomResponse> gameRooms = gameRoomRepository.findAllByRoomStatus(WAITING).stream()
                .map(gameRoomMapper::toGameRoomResponse)
                .toList();

        ListGameRoomResponse listGameRoomResponse = new ListGameRoomResponse();
        listGameRoomResponse.setRooms(gameRooms);

        return listGameRoomResponse;
    }

    @Override
    public void leaveGameRoom(Long roomId, MyUserDetails myUserDetails) {
        User user = userRepository.findByEmail(myUserDetails.getEmail())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        GameRoom gameRoom = gameRoomRepository.findWithLockById(roomId)
                .orElseThrow(() -> new GameRoomNotFoundException(GAME_ROOM_NOT_FOUND));

        if (gameRoom.getRoomStatus() != WAITING)
            throw new GameRoomNotWaitingException(GAME_ROOM_NOT_WAITING);

        gameParticipantRepository.deleteByUserAndGameRoom(user, gameRoom);

        gameRoom.removeCurrentPlayer();
    }

    @Override
    public void removeGameRoom(Long roomId, MyUserDetails myUserDetails) {
        User user = userRepository.findByEmail(myUserDetails.getEmail())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        GameRoom gameRoom = gameRoomRepository.findWithLockById(roomId)
                .orElseThrow(() -> new GameRoomNotFoundException(GAME_ROOM_NOT_FOUND));

        if (gameRoom.getHostUser() != user) {
            throw new GameRoomDeleteForbiddenException(GAME_ROOM_DELETE_NOT_ALLOWED);
        }

        gameRoomRepository.delete(gameRoom);
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
                .gameRoomStatus(gameRoom.getRoomStatus().name())
                .maxPlayer(gameRoom.getMaxPlayer())
                .currentPlayer(gameRoom.getCurrentPlayer())
                .isHost(gameRoom.getHostUser() == user)
                .participantResponseList(gameParticipantResponseList)
                .build();
    }
}
