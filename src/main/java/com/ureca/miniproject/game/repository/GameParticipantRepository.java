package com.ureca.miniproject.game.repository;

import com.ureca.miniproject.game.entity.GameParticipant;
import com.ureca.miniproject.game.entity.ParticipantStatus;
import com.ureca.miniproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameParticipantRepository extends JpaRepository<GameParticipant, Long> {

    Boolean existsByUserAndStatus(User user, ParticipantStatus status);
}
