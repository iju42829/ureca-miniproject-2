package com.ureca.miniproject.game.service;

import com.ureca.miniproject.config.MyUserDetails;
import com.ureca.miniproject.game.controller.request.CreateRoomRequest;
import com.ureca.miniproject.game.entity.GameParticipant;
import com.ureca.miniproject.game.entity.GameRoom;
import com.ureca.miniproject.game.entity.ParticipantStatus;
import com.ureca.miniproject.game.repository.GameRoomRepository;
import com.ureca.miniproject.game.service.response.CreateGameRoomResponse;
import com.ureca.miniproject.user.entity.User;
import com.ureca.miniproject.user.exception.UserNotFoundException;
import com.ureca.miniproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ureca.miniproject.common.BaseCode.USER_NOT_FOUND;
import static com.ureca.miniproject.game.entity.RoomStatus.WAITING;

@Service
@Transactional
@RequiredArgsConstructor
public class GameRoomServiceImpl implements GameRoomService {

    private final GameRoomRepository gameRoomRepository;
    private final UserRepository userRepository;

    @Override
    public CreateGameRoomResponse createGameRoom(CreateRoomRequest createRoomRequest, MyUserDetails myUserDetails) {
        User user = userRepository.findByEmail(myUserDetails.getEmail())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        GameParticipant gameParticipant = GameParticipant.builder()
                .user(user)
                .status(ParticipantStatus.JOINED)
                .isAlive(true)
                .build();

        GameRoom gameRoom = GameRoom.createGameRoom(user, createRoomRequest.getTitle(), WAITING, createRoomRequest.getMaxPlayer(), gameParticipant);

        gameRoomRepository.save(gameRoom);

        return new CreateGameRoomResponse(gameRoom.getId());
    }
}
