package com.ureca.miniproject.game.service;

import com.ureca.miniproject.config.MyUserDetails;
import com.ureca.miniproject.game.controller.request.CreateRoomRequest;
import com.ureca.miniproject.game.service.response.CreateGameRoomResponse;
import com.ureca.miniproject.game.service.response.GameRoomDetailResponse;
import com.ureca.miniproject.game.service.response.ListGameRoomResponse;

public interface GameRoomService {

    CreateGameRoomResponse createGameRoom(CreateRoomRequest createRoomRequest, MyUserDetails myUserDetails);
    ListGameRoomResponse listGameRoom();
    GameRoomDetailResponse getGameRoomDetail(Long roomId, MyUserDetails myUserDetails);
    void leaveGameRoom(Long roomId, MyUserDetails myUserDetails);
    void removeGameRoom(Long roomId, MyUserDetails myUserDetails);
}
