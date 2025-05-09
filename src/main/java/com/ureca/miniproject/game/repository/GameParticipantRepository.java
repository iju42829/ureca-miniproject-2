package com.ureca.miniproject.game.repository;

import com.ureca.miniproject.game.entity.GameParticipant;
import com.ureca.miniproject.game.entity.ParticipantStatus;
import com.ureca.miniproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameParticipantRepository extends JpaRepository<GameParticipant, Long> {

    Boolean existsByUserAndStatus(User user, ParticipantStatus status);
    List<GameParticipant> findAllByGameRoom_Id(Long roomId);
    Optional<GameParticipant> findByUserAndStatus(User user, ParticipantStatus status);
}
