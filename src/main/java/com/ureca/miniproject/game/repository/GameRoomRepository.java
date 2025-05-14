package com.ureca.miniproject.game.repository;

import com.ureca.miniproject.game.entity.GameRoom;
import com.ureca.miniproject.game.entity.RoomStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface GameRoomRepository extends JpaRepository<GameRoom, Long> {
    List<GameRoom> findAllByRoomStatus(RoomStatus roomStatus);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<GameRoom> findWithLockById(Long id);
}
