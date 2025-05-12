package com.ureca.miniproject.game.repository;

import com.ureca.miniproject.game.entity.GameResult;
import com.ureca.miniproject.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameResultRepository extends JpaRepository<GameResult, Long> {
    @EntityGraph(attributePaths = {"gameRoom", "gameRoom.hostUser"})
    List<GameResult> findAllByUser(User user);
}
