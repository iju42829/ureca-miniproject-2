package com.ureca.miniproject.game.service;

import com.ureca.miniproject.config.MyUserDetails;
import com.ureca.miniproject.game.controller.request.CreateRoomRequest;
import com.ureca.miniproject.game.service.response.CreateGameRoomResponse;

public interface GameRoomService {

    CreateGameRoomResponse createGameRoom(CreateRoomRequest createRoomRequest, MyUserDetails myUserDetails);
}
