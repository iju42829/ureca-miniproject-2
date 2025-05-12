package com.ureca.miniproject.game.repository;

import com.ureca.miniproject.game.entity.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameResultRepository extends JpaRepository<GameResult, Long> {
}
