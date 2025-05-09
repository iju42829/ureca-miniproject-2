package com.ureca.miniproject.game.repository;

import com.ureca.miniproject.game.entity.GameRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRoomRepository extends JpaRepository<GameRoom, Long> {
}
