package com.ureca.miniproject.game.repository;

import com.ureca.miniproject.game.entity.GameRoom;
import com.ureca.miniproject.game.entity.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRoomRepository extends JpaRepository<GameRoom, Long> {
    List<GameRoom> findAllByRoomStatus(RoomStatus roomStatus);
}
